package whiteboard.persistence.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

import javax.sql.DataSource;

public class Transaction implements Runnable {
    private final DataSource ds;
    private final Consumer<DataSource> consumer;

    public Transaction(DataSource ds, Consumer<DataSource> consumer) {
        this.ds = ds;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try (final SingleConnectionDataSource sds = new SingleConnectionDataSource(ds);
             final Connection con = sds.getConnection()) {
            con.setAutoCommit(false);
            consumer.accept(sds);
            con.commit();
        } catch (SQLException | IOException e) {
            throw new Repository.RepositoryException(e);
        }
    }
}
