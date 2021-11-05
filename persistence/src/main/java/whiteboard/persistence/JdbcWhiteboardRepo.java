package whiteboard.persistence;

import org.h2.jdbcx.JdbcDataSource;
import whiteboard.Whiteboard;
import whiteboard.WhiteboardRepo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private final DataSource ds;

    public JdbcWhiteboardRepo() {
        this.ds = createDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }

    private static DataSource createDataSource(String url) {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(url);
        return ds;
    }

    private Connection establishConnection() {
        // TODO use connection pool
        // TODO connection string should be configurable
        try {
            final Connection connection = ds.getConnection();
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
        return findOne(
            "select id, name from whiteboard_records where id = ?",
            ps -> ps.setLong(1, id)
        );
    }

    @Override
    public Whiteboard findByName(String name) {
        if (name == null) {
            return null;
        }
        return findOne(
            "select id, name from whiteboard_records where name = ?",
            ps -> ps.setString(1, name)
        );
    }

    private Whiteboard findOne(String sql, PreparedStatementParameterSetter paramSetter) {
        try (var con = establishConnection();
             var ps = con.prepareStatement(sql)) {
            ps.setFetchSize(1);
            ps.setQueryTimeout(10);
            paramSetter.accept(ps);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Whiteboard(
                        rs.getString("name"),
                        rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @FunctionalInterface
    private interface PreparedStatementParameterSetter {
        void accept(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    private interface ResultSetMapper<T> {
        T apply(ResultSet ps) throws SQLException;
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
