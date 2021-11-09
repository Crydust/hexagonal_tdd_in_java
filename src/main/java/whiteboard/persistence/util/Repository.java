package whiteboard.persistence.util;

import static java.util.Objects.requireNonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

public final class Repository {

    private Repository() {
        throw new UnsupportedOperationException("this class is not supposed to be instantiated");
    }

    public static <T> Optional<T> sqlToOptional(DataSource ds, SqlWithParameters sqlWithParameters, ResultSetMapper<T> resultSetMapper) throws RepositoryException {
        final List<T> list = sqlToList(ds, sqlWithParameters, resultSetMapper);
        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    private static <T> List<T> sqlToList(DataSource ds, SqlWithParameters sql, ResultSetMapper<T> resultSetMapper) throws RepositoryException {
        requireNonNull(ds, "ds");
        requireNonNull(sql, "sql");
        requireNonNull(resultSetMapper, "resultSetMapper");
        final List<T> list = new ArrayList<>();
        try (Connection con = ds.getConnection()) {
            con.setReadOnly(true);
            try (PreparedStatement ps = con.prepareStatement(sql.getSql())) {
                ps.setQueryTimeout(sql.getQueryTimeout());
                ps.setFetchSize(sql.getFetchSize());
                sql.accept(ps);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(resultSetMapper.map(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Could not execute query '" + sql + "'", e);
        }
        return list;
    }

    public static int executeUpdate(DataSource ds, SqlWithParameters sql) throws RepositoryException {
        return executeUpdate(ds, sql, null);
    }

    public static int executeUpdate(DataSource ds, SqlWithParameters sql, GeneratedKey generatedKey) throws RepositoryException {
        requireNonNull(ds, "ds");
        requireNonNull(sql, "sql");
        try (Connection con = ds.getConnection()) {
            con.setReadOnly(false);
            try (PreparedStatement ps = generatedKey == null
                ? con.prepareStatement(sql.getSql())
                : con.prepareStatement(sql.getSql(), generatedKey.getColumnNames())) {
                ps.setQueryTimeout(sql.getQueryTimeout());
                sql.accept(ps);
                final int rowCount = ps.executeUpdate();
                if (rowCount == 1 && generatedKey != null) {
                    generatedKey.read(ps);
                }
                return rowCount;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Could not execute update '" + sql + "'", e);
        }
    }

    @FunctionalInterface
    public interface ParameterSetter {
        void accept(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    public static class GeneratedKey {
        private final String columnName;
        private Long columnValue;

        public GeneratedKey(String columnName) {
            this.columnName = columnName;
        }

        public String[] getColumnNames() {
            return new String[]{columnName};
        }

        public Long getColumnValue() {
            return columnValue;
        }

        public void read(PreparedStatement ps) throws SQLException {
            try (var generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    columnValue = generatedKeys.getLong(columnName);
                } else {
                    throw new RepositoryException("no generated keys?");
                }
            }
        }
    }

    public static class RepositoryException extends RuntimeException {
        public RepositoryException() {
        }

        public RepositoryException(String message) {
            super(message);
        }

        public RepositoryException(String message, Throwable cause) {
            super(message, cause);
        }

        public RepositoryException(Throwable cause) {
            super(cause);
        }

        public RepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }


}
