package whiteboard.util.persistence;

record FetchSize(int rows) {
    public static final FetchSize DEFAULT_FETCH_SIZE = new FetchSize(0);
    public static final FetchSize HUNDRED_ROWS_FETCH_SIZE = new FetchSize(100);
    public static final FetchSize ONE_ROW_FETCH_SIZE = new FetchSize(1);

    FetchSize {
        if (rows < 0) {
            throw new IllegalArgumentException("rows must be positive and '" + rows + "' is not");
        }
    }

}
