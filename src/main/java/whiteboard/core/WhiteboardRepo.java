package whiteboard.core;

import java.util.List;

public interface WhiteboardRepo {
    default void initialize() throws Exception {
    }

    default void dispose() throws Exception {
    }

    Whiteboard findById(Long id);

    Whiteboard findByName(String name);

    List<Whiteboard> findAll();

    void save(Whiteboard whiteboard);

    void deleteAll();
}

