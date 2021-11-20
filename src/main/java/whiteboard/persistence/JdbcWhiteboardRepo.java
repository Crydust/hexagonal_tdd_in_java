package whiteboard.persistence;

import org.h2.jdbcx.JdbcConnectionPool;
import whiteboard.core.Whiteboard;
import whiteboard.core.WhiteboardRepo;
import whiteboard.persistence.util.Repository.GeneratedKey;
import whiteboard.persistence.util.SqlWithParameters;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static whiteboard.persistence.util.Repository.executeUpdate;
import static whiteboard.persistence.util.Repository.sqlToList;
import static whiteboard.persistence.util.Repository.sqlToOptional;

public class JdbcWhiteboardRepo implements WhiteboardRepo {

    private static final List<Migration> MIGRATIONS = List.of(new CreateWhiteboardRecordTableMigration());
    private static final AtomicBoolean MIGRATIONS_DONE = new AtomicBoolean();
    private JdbcConnectionPool ds = null;

    @Override
    public void initialize() throws Exception {
        ds = JdbcConnectionPool.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "");
        if (!MIGRATIONS_DONE.get()) {
            try (Connection connection = ds.getConnection()) {
                for (Migration migration : MIGRATIONS) {
                    migration.perform(connection);
                }
            }
            MIGRATIONS_DONE.set(true);
        }
    }

    @Override
    public void dispose() {
        ds.dispose();
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

    @Override
    public List<Whiteboard> findAll() {
        return sqlToList(ds,
            new SqlWithParameters("select id, name from whiteboard_records order by name"),
            JdbcWhiteboardRepo::map
        );
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
