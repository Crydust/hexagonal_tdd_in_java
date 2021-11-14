package whiteboard.core;

public interface WhiteboardRepo {
    default void initialize() throws Exception {
    }

    default void dispose() throws Exception {
    }

    Whiteboard findById(Long id);

    Whiteboard findByName(String name);

    void save(Whiteboard whiteboard);

    void deleteAll();
}

