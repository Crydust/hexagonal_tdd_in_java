package whiteboard.core;

import java.util.Objects;

public class Whiteboard {
    private final String name;
    private Long id;

    public Whiteboard(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Whiteboard that = (Whiteboard) o;
        return Objects.equals(name, that.name) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return "Whiteboard{" +
            "name='" + name + '\'' +
            ", id=" + id +
            '}';
    }
}
