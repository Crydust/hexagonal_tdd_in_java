package whiteboard.persistence;

import whiteboard.FakeWhiteboardRepo;

public class FakeWhiteboardRepo_ConformsToWhiteboardRepoContractTest extends WhiteboardRepoContract {
    @Override
    protected void createRepo() {
        repo = new FakeWhiteboardRepo();
    }
}
