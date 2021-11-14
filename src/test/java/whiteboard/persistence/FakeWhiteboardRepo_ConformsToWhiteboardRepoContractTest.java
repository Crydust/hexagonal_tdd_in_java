package whiteboard.persistence;

import whiteboard.core.FakeWhiteboardRepo;
import whiteboard.core.WhiteboardRepo;

public class FakeWhiteboardRepo_ConformsToWhiteboardRepoContractTest extends WhiteboardRepoContract {
    @Override
    protected WhiteboardRepo createRepo() {
        return new FakeWhiteboardRepo();
    }
}
