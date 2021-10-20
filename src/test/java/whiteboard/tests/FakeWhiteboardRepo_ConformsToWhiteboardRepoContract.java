package whiteboard.tests;

public class FakeWhiteboardRepo_ConformsToWhiteboardRepoContract extends WhiteboardRepoContractTest {
    @Override
    protected void createRepo() {
        repo = new FakeWhiteboardRepo();
    }
}
