package whiteboard.util.persistence;

final class FetchSize {
    public static final FetchSize DEFAULT_FETCH_SIZE = new FetchSize(0);
    public static final FetchSize HUNDRED_ROWS_FETCH_SIZE = new FetchSize(100);
    public static final FetchSize ONE_ROW_FETCH_SIZE = new FetchSize(1);

    private final int rows;

    public FetchSize(int rows) {
        if (rows < 0) {
            throw new IllegalArgumentException("rows must be positive and '" + rows + "' is not");
        }
        this.rows = rows;
    }

    public int getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "FetchSize{" +
                "rows=" + rows +
                '}';
    }
}
