package whiteboard.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class CreateWhiteboardRecordTableMigration implements Migration {
    @Override
    public void perform(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            st.setQueryTimeout(10);
            st.execute("create sequence whiteboard_seq");
            st.execute("create table whiteboard_records (id long primary key, name varchar(255) not null unique)");
        }
    }
}
