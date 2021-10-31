package whiteboard.servlets;

import whiteboard.FakeWhiteboardRepo;
import whiteboard.WhiteboardRepo;

final class Configuration {
    static final WhiteboardRepo WHITEBOARD_REPO = new FakeWhiteboardRepo();
}
