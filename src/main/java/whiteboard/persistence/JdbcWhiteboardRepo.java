package whiteboard.persistence;

import static whiteboard.persistence.util.Repository.executeUpdate;
import static whiteboard.persistence.util.Repository.sqlToOptional;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

import whiteboard.core.Whiteboard;
import whiteboard.core.WhiteboardRepo;
import whiteboard.persistence.util.Repository.GeneratedKey;
import whiteboard.persistence.util.SqlWithParameters;

public class JdbcWhiteboardRepo implements WhiteboardRepo {

    private static volatile boolean initialized;
    private static final Semaphore INITIALIZING = new Semaphore(1, false);
    private static final CountDownLatch INIT_DONE = new CountDownLatch(1);
    private static final List<Migration> MIGRATIONS = List.of(new CreateWhiteboardRecordTable());
    private final DataSource ds;

    public JdbcWhiteboardRepo() {
        this.ds = createDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }

    private static DataSource createDataSource(String url) {
        // TODO use connection pool
        // TODO connection string should be configurable
        try {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);
            initializeDatabase(ds.getConnection());
            return ds;
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
                    if (INITIALIZING.tryAcquire()) {
                        for (Migration migration : MIGRATIONS) {
                            migration.perform(connection);
                        }
                        initialized = true;
                        INIT_DONE.countDown();
                    } else {
                        if (!INIT_DONE.await(10, TimeUnit.SECONDS)) {
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
            try (Statement st = con.createStatement()) {
                st.setQueryTimeout(10);
                st.execute("create sequence whiteboard_seq");
                st.execute("create table whiteboard_records (id long primary key, name varchar(255) not null unique)");
            }
        }
    }

    @Override
    public Whiteboard findById(Long id) {
        if (id == null) {
            return null;
        }
        return sqlToOptional(
            ds,
            new SqlWithParameters("select id, name from whiteboard_records where id = ?")
                .setLong(1, id),
            JdbcWhiteboardRepo::map
        ).orElse(null);
    }

    @Override
    public Whiteboard findByName(String name) {
        if (name == null) {
            return null;
        }
        return sqlToOptional(
            ds,
            new SqlWithParameters("select id, name from whiteboard_records where name = ?")
                .setString(1, name),
            JdbcWhiteboardRepo::map
        ).orElse(null);
    }

    private static Whiteboard map(ResultSet rs) throws SQLException {
        return new Whiteboard(
            rs.getString("name"),
            rs.getLong("id"));
    }

    @Override
    public void save(Whiteboard whiteboard) {
        final GeneratedKey generatedKey = new GeneratedKey("id");
        executeUpdate(
            ds,
            new SqlWithParameters("insert into whiteboard_records (id, name) values (next value for whiteboard_seq, ?)")
                .setString(1, whiteboard.getName()),
            generatedKey
        );
        whiteboard.setId(generatedKey.getColumnValue());
    }

    @Override
    public void deleteAll() {
        executeUpdate(
            ds,
            new SqlWithParameters("truncate table whiteboard_records")
        );
    }
}
