package whiteboard.persistence;

import java.sql.Connection;
import java.sql.SQLException;

interface Migration {
    void perform(Connection con) throws SQLException;
}
