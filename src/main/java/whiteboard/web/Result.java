package whiteboard.web;

import java.util.function.Consumer;

public final class Result<S, F> {
    private final S success;
    private final F failure;
    private final boolean isSuccess;

    private Result(S success, F failure, boolean isSuccess) {
        this.success = success;
        this.failure = failure;
        this.isSuccess = isSuccess;
    }

    public static <S, F> Result<S, F> success(S success) {
        return new Result<>(success, null, true);
    }

    public static <S, F> Result<S, F> fail(F failure) {
        return new Result<>(null, failure, false);
    }

    public void then(Consumer<S> successConsumer, Consumer<F> failureConsumer) {
        if (isSuccess) {
            successConsumer.accept(success);
        } else {
            failureConsumer.accept(failure);
        }
    }
}
