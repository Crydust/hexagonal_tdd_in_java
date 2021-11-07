package whiteboard.persistence;

import whiteboard.core.FakeWhiteboardRepo;

public class FakeWhiteboardRepo_ConformsToWhiteboardRepoContractTest extends WhiteboardRepoContract {
    @Override
    protected void createRepo() {
        repo = new FakeWhiteboardRepo();
    }
}
