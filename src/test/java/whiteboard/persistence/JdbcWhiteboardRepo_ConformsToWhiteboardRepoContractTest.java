package whiteboard.persistence;

import whiteboard.core.WhiteboardRepo;

public class JdbcWhiteboardRepo_ConformsToWhiteboardRepoContractTest extends WhiteboardRepoContract {
    @Override
    protected WhiteboardRepo createRepo() {
        return new JdbcWhiteboardRepo();
    }
}
