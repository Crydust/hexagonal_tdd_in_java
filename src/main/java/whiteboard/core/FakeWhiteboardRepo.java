package whiteboard.core;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class FakeWhiteboardRepo implements WhiteboardRepo {
    private final ArrayList<Whiteboard> whiteboards = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Whiteboard findById(Long id) {
        return whiteboards.stream()
            .filter(w -> w.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

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
        whiteboard.setId(sequence.incrementAndGet());
    }

    @Override
    public void deleteAll() {
        whiteboards.clear();
    }
}
