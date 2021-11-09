package whiteboard.web;

import whiteboard.core.WhiteboardRepo;
import whiteboard.persistence.JdbcWhiteboardRepo;

final class Configuration {
    private Configuration() {
        // NOOP
    }

    //    static final WhiteboardRepo WHITEBOARD_REPO = new FakeWhiteboardRepo();
    static final WhiteboardRepo WHITEBOARD_REPO = new JdbcWhiteboardRepo();
}
