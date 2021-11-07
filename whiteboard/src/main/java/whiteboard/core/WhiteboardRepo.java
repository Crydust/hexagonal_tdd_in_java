package whiteboard.core;

public interface WhiteboardRepo {
    Whiteboard findById(Long id);

    Whiteboard findByName(String name);

    void save(Whiteboard whiteboard);

    void deleteAll();
}

