package whiteboard.util.persistence;

record QueryTimeout(int seconds) {
    public static final QueryTimeout DEFAULT_QUERY_TIMEOUT = new QueryTimeout(5);

    QueryTimeout {
        if (seconds < 0) {
            throw new IllegalArgumentException("seconds must be positive and '" + seconds + "' is not");
        }
    }

}
