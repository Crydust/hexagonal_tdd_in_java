package whiteboard.persistence;

public class JdbcWhiteboardRepo_ConformsToWhiteboardRepoContractTest extends WhiteboardRepoContract {
    @Override
    protected void createRepo() {
        repo = new JdbcWhiteboardRepo();
    }
}
