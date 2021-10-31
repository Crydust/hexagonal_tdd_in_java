package whiteboard.persistence;

public class JdbcWhiteboardRepo_ConformsToWhiteboardRepoContractTest extends WhiteboardRepoContractTest {
    @Override
    protected void createRepo() {
        repo = new JdbcWhiteboardRepo();
    }
}
