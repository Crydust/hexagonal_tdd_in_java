package whiteboard;

import java.io.Serializable;

public record ValidationError(String field, String errorCode) implements Serializable {
}
