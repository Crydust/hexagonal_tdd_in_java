package whiteboard.tests;

import org.junit.jupiter.api.Test;
import whiteboard.CreateWhiteboardObserver;
import whiteboard.FakeWhiteboardRepo;
import whiteboard.UseCases;
import whiteboard.ValidationError;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.notNullValue;

class CreateWhiteboardTest {
    final CreateWhiteboardObserverSpy spy = new CreateWhiteboardObserverSpy();
    final FakeWhiteboardRepo repo = new FakeWhiteboardRepo();

    @Test
    void nameIsRequired() {
        UseCases.createWhiteboard(null, spy, repo);

        assertReceivedError("name", "required");
    }

    @Test
    void nameDupeNotAllowed() {
        UseCases.createWhiteboard("whiteboard name", spy, repo);
        UseCases.createWhiteboard("whiteboard name", spy, repo);

        assertReceivedError("name", "unique");
    }

    @Test
    void givenValidName_sendsIdBackToObserver() {
        UseCases.createWhiteboard("whiteboard name", spy, repo);

        assertThat(spy.spyId(), notNullValue());
    }

    private void assertReceivedError(String field, String errorCode) {
        assertThat(
            spy.spyValidationErrors(),
            contains(new ValidationError(field, errorCode))
        );
    }

    static class CreateWhiteboardObserverSpy implements CreateWhiteboardObserver {
        private List<ValidationError> spyValidationErrors;
        private Object id;

        public List<ValidationError> spyValidationErrors() {
            return spyValidationErrors;
        }

        @Override
        public void validationFailed(List<ValidationError> errors) {
            spyValidationErrors = errors;
        }

        @Override
        public void whiteboardCreated(Long id) {
            this.id = id;
        }

        public Object spyId() {
            return id;
        }
    }
}
