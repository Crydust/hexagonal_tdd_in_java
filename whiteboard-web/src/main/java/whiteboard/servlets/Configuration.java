package whiteboard.servlets;

import whiteboard.WhiteboardRepo;
import whiteboard.persistence.JdbcWhiteboardRepo;

final class Configuration {
    //    static final WhiteboardRepo WHITEBOARD_REPO = new FakeWhiteboardRepo();
    static final WhiteboardRepo WHITEBOARD_REPO = new JdbcWhiteboardRepo();
}