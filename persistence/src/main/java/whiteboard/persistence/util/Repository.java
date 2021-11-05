package whiteboard.persistence.util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static whiteboard.persistence.util.FetchSize.HUNDRED_ROWS_FETCH_SIZE;
import static whiteboard.persistence.util.FetchSize.ONE_ROW_FETCH_SIZE;
import static whiteboard.persistence.util.QueryTimeout.DEFAULT_QUERY_TIMEOUT;

public final class Repository {

    private Repository() {
        throw new UnsupportedOperationException("this class is not supposed to be instantiated");
    }

//    public static DataSource lookupDataSource() throws RepositoryException {
//        final String contextName = "java:comp/env";
//        final String datasourceName = "jdbc/MyDataSource";
//        try {
//            final Context ctx = (Context) new InitialContext().lookup(contextName);
//            return (DataSource) ctx.lookup(datasourceName);
//        } catch (NamingException e) {
//            throw new RepositoryException("Could not find DataSource '" + datasourceName + "' in context '" + contextName + "'", e);
//        }
//    }

    public static <T> Optional<T> sqlToOptional(DataSource ds, SqlWithParameters sqlWithParameters, ResultSetMapper<T> resultSetMapper) throws RepositoryException {
        try {
            final List<T> list = sqlToList(ds, sqlWithParameters.getSql(), sqlWithParameters, resultSetMapper, new QueryTimeout(sqlWithParameters.getQueryTimeout()), new FetchSize(sqlWithParameters.getFetchSize()));
            return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private static <T> Optional<T> sqlToOptional(DataSource ds, String sql, ParameterSetter parameterSetter, ResultSetMapper<T> resultSetMapper) throws RepositoryException {
        final List<T> list = sqlToList(ds, sql, parameterSetter, resultSetMapper, DEFAULT_QUERY_TIMEOUT, ONE_ROW_FETCH_SIZE);
        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    public static <T> List<T> sqlToList(DataSource ds, SqlWithParameters sqlWithParameters, ResultSetMapper<T> resultSetMapper) throws RepositoryException {
        try {
            return sqlToList(ds, sqlWithParameters.getSql(), sqlWithParameters, resultSetMapper, new QueryTimeout(sqlWithParameters.getQueryTimeout()), new FetchSize(sqlWithParameters.getFetchSize()));
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private static <T> List<T> sqlToList(DataSource ds, String sql, ParameterSetter parameterSetter, ResultSetMapper<T> resultSetMapper) throws RepositoryException {
        return sqlToList(ds, sql, parameterSetter, resultSetMapper, DEFAULT_QUERY_TIMEOUT, HUNDRED_ROWS_FETCH_SIZE);
    }

    private static <T> List<T> sqlToList(DataSource ds, String sql, ParameterSetter parameterSetter, ResultSetMapper<T> resultSetMapper, QueryTimeout queryTimeout, FetchSize fetchSize) throws RepositoryException {
        requireNonNull(ds, "ds");
        requireNonNull(sql, "sql");
        requireNonNull(parameterSetter, "parameterSetter");
        requireNonNull(resultSetMapper, "resultSetMapper");
        requireNonNull(queryTimeout, "queryTimeout");
        requireNonNull(fetchSize, "fetchSize");
        final List<T> list = new ArrayList<>();
        try (final Connection con = ds.getConnection()) {
            con.setReadOnly(true);
            try (final PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setQueryTimeout(queryTimeout.getSeconds());
                ps.setFetchSize(fetchSize.getRows());
                parameterSetter.accept(ps);
                try (final ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(resultSetMapper.map(rs));
                    }
                }
            }
        } catch (SQLException e) {
            final String sqlOnOneLine = sql.replaceAll("[\r\n]+", " ");
            throw new RepositoryException("Could not execute query '" + sqlOnOneLine + "'", e);
        }
        return list;
    }

    public static int executeUpdate(DataSource ds, String sql, ParameterSetter parameterSetter) throws RepositoryException {
        return executeUpdate(ds, sql, parameterSetter, DEFAULT_QUERY_TIMEOUT);
    }

    private static int executeUpdate(DataSource ds, String sql, ParameterSetter parameterSetter, QueryTimeout queryTimeout) throws RepositoryException {
        requireNonNull(ds, "ds");
        requireNonNull(sql, "sql");
        requireNonNull(parameterSetter, "parameterSetter");
        requireNonNull(queryTimeout, "queryTimeout");
        try (final Connection con = ds.getConnection()) {
            con.setReadOnly(false);
            try (final PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setQueryTimeout(queryTimeout.getSeconds());
                parameterSetter.accept(ps);
                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            final String sqlOnOneLine = sql.replaceAll("[\r\n]+", " ");
            throw new RepositoryException("Could not execute update '" + sqlOnOneLine + "'", e);
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

    public static class RepositoryException extends RuntimeException {
        public RepositoryException() {
            super();
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
