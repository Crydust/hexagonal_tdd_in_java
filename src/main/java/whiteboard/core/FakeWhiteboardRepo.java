package whiteboard.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Comparator.comparing;

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
    public List<Whiteboard> findAll() {
        return whiteboards.stream()
            .sorted(comparing(Whiteboard::getName))
            .toList();
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
