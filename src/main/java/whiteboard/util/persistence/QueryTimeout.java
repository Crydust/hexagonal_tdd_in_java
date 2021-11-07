package whiteboard.util.persistence;

final class QueryTimeout {
    public static final QueryTimeout DEFAULT_QUERY_TIMEOUT = new QueryTimeout(5);

    private final int seconds;

    public QueryTimeout(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("seconds must be positive and '" + seconds + "' is not");
        }
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public String toString() {
        return "QueryTimeout{" +
            "seconds=" + seconds +
            '}';
    }
}
