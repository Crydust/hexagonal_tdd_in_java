package whiteboard.web;

import java.util.List;
import java.util.function.Consumer;

import whiteboard.core.CreateWhiteboardObserver;
import whiteboard.core.ValidationError;

final class Gui implements CreateWhiteboardObserver {
    private Result<Long, List<ValidationError>> result = Result.fail(List.of());

    @Override
    public void validationFailed(List<ValidationError> errors) {
        result = Result.fail(errors);
    }

    @Override
    public void whiteboardCreated(Long id) {
        result = Result.success(id);
    }

    public void then(Consumer<Long> successConsumer, Consumer<List<ValidationError>> failureConsumer) {
        result.then(successConsumer, failureConsumer);
    }
}
