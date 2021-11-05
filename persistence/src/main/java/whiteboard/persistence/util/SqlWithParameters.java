package whiteboard.persistence.util;

import java.sql.Date;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static whiteboard.persistence.util.FetchSize.DEFAULT_FETCH_SIZE;
import static whiteboard.persistence.util.QueryTimeout.DEFAULT_QUERY_TIMEOUT;

public class SqlWithParameters implements Repository.ParameterSetter {

    private final StringBuilder sql;
    private final List<Parameter> parameters = new ArrayList<>();
    private QueryTimeout queryTimeout = DEFAULT_QUERY_TIMEOUT;
    private FetchSize fetchSize = DEFAULT_FETCH_SIZE;

    private static final class Parameter {
        private final int index;
        private final JDBCType type;
        private final Object value;

        private Parameter(int index, JDBCType type, Object value) {
            this.index = index;
            this.type = type;
            this.value = value;
        }
    }

    private static final class TimeWithTimeZone {
        private final Time time;
        private final Calendar calendar;

        private TimeWithTimeZone(Time time, Calendar calendar) {
            this.time = time;
            this.calendar = calendar;
        }

        @Override
        public String toString() {
            return "TimeWithTimeZone{" +
                "time=" + time +
                ", calendar=" + calendar +
                '}';
        }
    }

    private static final class TimestampWithTimeZone {
        private final Timestamp timestamp;
        private final Calendar calendar;

        private TimestampWithTimeZone(Timestamp timestamp, Calendar calendar) {
            this.timestamp = timestamp;
            this.calendar = calendar;
        }

        @Override
        public String toString() {
            return "TimestampWithTimeZone{" +
                "timestamp=" + timestamp +
                ", calendar=" + calendar +
                '}';
        }
    }

    public SqlWithParameters() {
        this(new StringBuilder());
    }

    public SqlWithParameters(String sql) {
        this(new StringBuilder(sql));
    }

    private SqlWithParameters(StringBuilder sql) {
        this.sql = new StringBuilder(sql);
    }

    public SqlWithParameters setNull(int parameterIndex, int sqlType) {
        return setObject(parameterIndex, null, JDBCType.valueOf(sqlType).getVendorTypeNumber());
    }

    public SqlWithParameters setBoolean(int parameterIndex, boolean x) {
        return setObject(parameterIndex, x, JDBCType.BOOLEAN.getVendorTypeNumber());
    }

    public SqlWithParameters setByte(int parameterIndex, byte x) {
        return setObject(parameterIndex, x, JDBCType.TINYINT.getVendorTypeNumber());
    }

    public SqlWithParameters setShort(int parameterIndex, short x) {
        return setObject(parameterIndex, x, JDBCType.SMALLINT.getVendorTypeNumber());
    }

    public SqlWithParameters setInt(int parameterIndex, int x) {
        return setObject(parameterIndex, x, JDBCType.INTEGER.getVendorTypeNumber());
    }

    public SqlWithParameters setLong(int parameterIndex, long x) {
        return setObject(parameterIndex, x, JDBCType.BIGINT.getVendorTypeNumber());
    }

    public SqlWithParameters setFloat(int parameterIndex, float x) {
        return setObject(parameterIndex, x, JDBCType.FLOAT.getVendorTypeNumber());
    }

    public SqlWithParameters setDouble(int parameterIndex, double x) {
        return setObject(parameterIndex, x, JDBCType.FLOAT.getVendorTypeNumber());
    }

    public SqlWithParameters setString(int parameterIndex, String x) {
        return setObject(parameterIndex, x, JDBCType.VARCHAR.getVendorTypeNumber());
    }

    public SqlWithParameters setDate(int parameterIndex, Date x) {
        return setObject(parameterIndex, x, JDBCType.DATE.getVendorTypeNumber());
    }

    public SqlWithParameters setTime(int parameterIndex, Time x) {
        return setObject(parameterIndex, x, JDBCType.TIME.getVendorTypeNumber());
    }

    public SqlWithParameters setTimestamp(int parameterIndex, Timestamp x) {
        return setObject(parameterIndex, x, JDBCType.TIMESTAMP.getVendorTypeNumber());
    }

    public SqlWithParameters setObject(int parameterIndex, Object x, int targetSqlType) {
        parameters.add(new Parameter(parameterIndex, JDBCType.valueOf(targetSqlType), x));
        return this;
    }

    public SqlWithParameters setTime(int parameterIndex, Time x, Calendar cal) {
        return setObject(parameterIndex, new TimeWithTimeZone(x, cal), JDBCType.TIME_WITH_TIMEZONE.getVendorTypeNumber());
    }

    public SqlWithParameters setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {
        return setObject(parameterIndex, new TimestampWithTimeZone(x, cal), JDBCType.TIMESTAMP_WITH_TIMEZONE.getVendorTypeNumber());
    }

    public SqlWithParameters setNull(int parameterIndex, int sqlType, String typeName) {
        return setObject(parameterIndex, null, JDBCType.valueOf(sqlType).getVendorTypeNumber());
    }

    public int getQueryTimeout() throws SQLException {
        return queryTimeout.getSeconds();
    }

    public SqlWithParameters setQueryTimeout(int seconds) {
        queryTimeout = new QueryTimeout(seconds);
        return this;
    }

    public SqlWithParameters setFetchSize(int rows) {
        fetchSize = new FetchSize(rows);
        return this;
    }

    public int getFetchSize() throws SQLException {
        return fetchSize.getRows();
    }

    public String getSql() {
        return this.sql.toString();
    }

    public SqlWithParameters append(String sql) {
        this.sql.append(sql);
        return this;
    }

    public void accept(PreparedStatement ps) throws SQLException {
        for (Parameter parameter : parameters) {
            if (parameter.value == null) {
                ps.setNull(parameter.index, parameter.type.getVendorTypeNumber());
            } else {
                switch (parameter.type) {
                    case TINYINT:
                        ps.setByte(parameter.index, (byte) parameter.value);
                        break;
                    case SMALLINT:
                        ps.setShort(parameter.index, (short) parameter.value);
                        break;
                    case INTEGER:
                        ps.setInt(parameter.index, (int) parameter.value);
                        break;
                    case BIGINT:
                        ps.setLong(parameter.index, (long) parameter.value);
                        break;
                    case FLOAT:
                        ps.setFloat(parameter.index, (float) parameter.value);
                        break;
                    case DOUBLE:
                        ps.setDouble(parameter.index, (double) parameter.value);
                        break;
                    case VARCHAR:
                        ps.setString(parameter.index, (String) parameter.value);
                        break;
                    case DATE:
                        ps.setDate(parameter.index, (Date) parameter.value);
                        break;
                    case TIME:
                        ps.setTime(parameter.index, (Time) parameter.value);
                        break;
                    case TIME_WITH_TIMEZONE: {
                        final TimeWithTimeZone value = (TimeWithTimeZone) parameter.value;
                        ps.setTime(parameter.index, value.time, value.calendar);
                        break;
                    }
                    case TIMESTAMP:
                        ps.setTimestamp(parameter.index, (Timestamp) parameter.value);
                        break;
                    case TIMESTAMP_WITH_TIMEZONE: {
                        final TimestampWithTimeZone value = (TimestampWithTimeZone) parameter.value;
                        ps.setTimestamp(parameter.index, value.timestamp, value.calendar);
                        break;
                    }
                    case BOOLEAN:
                        ps.setBoolean(parameter.index, (boolean) parameter.value);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown type '" + parameter.type + "'");
                }
            }
        }
    }

    public String toString() {
        return "SqlWithParameters{" +
            "sql=" + sql +
            ", parameters=" + parameters +
            ", queryTimeout=" + queryTimeout +
            '}';
    }

}
