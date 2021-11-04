package whiteboard.persistence;

import whiteboard.Whiteboard;
import whiteboard.WhiteboardRepo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class JdbcWhiteboardRepo implements WhiteboardRepo {

    private static volatile boolean initialized = false;
    private static final Semaphore initializing = new Semaphore(1, false);
    private static final CountDownLatch initDone = new CountDownLatch(1);
    private static final List<Migration> migrations = List.of(new CreateWhiteboardRecordTable());
    private final String url;

    public JdbcWhiteboardRepo() {
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    }

    private Connection establishConnection() {
        // TODO use connection pool
        // TODO connection string should be configurable
        try {
            final Connection connection = DriverManager.getConnection(url);
            initializeDatabase(connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private static void initializeDatabase(Connection connection) throws SQLException, InterruptedException {
        if (!initialized) {
            synchronized (JdbcWhiteboardRepo.class) {
                if (!initialized) {
                    if (initializing.tryAcquire()) {
                        for (Migration migration : migrations) {
                            migration.perform(connection);
                        }
                        initialized = true;
                        initDone.countDown();
                    } else {
                        if (!initDone.await(10, TimeUnit.SECONDS)) {
                            throw new RuntimeException("Database init took too long");
                        }
                    }
                }
            }
        }
    }

    private interface Migration {
        void perform(Connection con) throws SQLException;
    }

    private static class CreateWhiteboardRecordTable implements Migration {
        @Override
        public void perform(Connection con) throws SQLException {
            try (final Statement st = con.createStatement()) {
                st.setQueryTimeout(10);
                st.execute("create sequence whiteboard_seq");
                st.execute("create table whiteboard_records (id long primary key, name varchar(255) default '' unique)");
            }
        }
    }

    @Override
    public Whiteboard findById(Long id) {
        if (id == null) {
            return null;
        }
        try (var con = establishConnection();
             var ps = con.prepareStatement("select id, name from whiteboard_records where id = ?")) {
            ps.setQueryTimeout(10);
            ps.setLong(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Whiteboard(
                        rs.getString("name"),
                        rs.getLong("id")
                    );
                }
                if (rs.next()) {
                    throw new RuntimeException("ambiguous?");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Whiteboard findByName(String name) {
        try (var con = establishConnection();
             var ps = con.prepareStatement("select id, name from whiteboard_records where name = ?")) {
            ps.setQueryTimeout(10);
            ps.setString(1, name);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Whiteboard(
                        rs.getString("name"),
                        rs.getLong("id")
                    );
                }
                if (rs.next()) {
                    throw new RuntimeException("ambiguous?");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void save(Whiteboard whiteboard) {
        try (var con = establishConnection();
             var ps = con.prepareStatement(
                 "insert into whiteboard_records (id, name) values (next value for whiteboard_seq, ?)",
                 new String[]{"id"}
             )) {
            ps.setQueryTimeout(10);
            ps.setString(1, whiteboard.getName());
            final int count = ps.executeUpdate();
            if (count == 1) {
                // success
                try (var generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        whiteboard.setId(generatedKeys.getLong("id"));
                    } else {
                        throw new RuntimeException("no generated keys?");
                    }
                }
            } else {
                throw new RuntimeException("count was not 1?");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        try (var con = establishConnection();
             var st = con.createStatement()) {
            st.setQueryTimeout(10);
            st.execute("truncate table whiteboard_records");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
