package whiteboard;

import java.util.ArrayList;
import java.util.UUID;

public class FakeWhiteboardRepo implements WhiteboardRepo {
    private final ArrayList<Whiteboard> whiteboards = new ArrayList<>();

    @Override
    public Whiteboard findByName(String name) {
        return whiteboards.stream()
            .filter(w -> w.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    @Override
    public void save(Whiteboard whiteboard) {
        whiteboards.add(whiteboard);
        whiteboard.setId(UUID.randomUUID().toString());
    }
}
