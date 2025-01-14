package whiteboard.core;

import java.util.ArrayList;

public final class UseCases {

    private UseCases() {
        // NOOP
    }

    public static void createWhiteboard(String name, CreateWhiteboardObserver gui, WhiteboardRepo repo) {
        new CreateWhiteboardUseCase(name, gui, repo).execute();
    }

    private static class CreateWhiteboardUseCase {
        private final String name;
        private final CreateWhiteboardObserver gui;
        private final WhiteboardRepo repo;
        private final ArrayList<ValidationError> errors = new ArrayList<>();
        private Whiteboard whiteboard;

        CreateWhiteboardUseCase(String name, CreateWhiteboardObserver gui, WhiteboardRepo repo) {
            this.name = name;
            this.gui = gui;
            this.repo = repo;
        }

        void execute() {
            if (nameTaken()) {
                reportError("name", "unique");
            } else if (nameNotProvided()) {
                reportError("name", "required");
            } else {
                saveWhiteboard();
                reportId();
            }
        }

        private void reportId() {
            gui.whiteboardCreated(whiteboard.getId());
        }

        private boolean nameNotProvided() {
            return name == null || name.isBlank();
        }

        private boolean nameTaken() {
            return repo.findByName(name) != null;
        }

        private void saveWhiteboard() {
            whiteboard = new Whiteboard(name, null);
            repo.save(whiteboard);
        }

        private void reportError(String field, String error) {
            errors.add(new ValidationError(field, error));
            gui.validationFailed(errors);
        }
    }
}
