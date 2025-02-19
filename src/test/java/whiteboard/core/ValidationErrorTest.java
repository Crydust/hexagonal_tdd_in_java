package whiteboard.core;

import org.junit.jupiter.api.Test;
import whiteboard.core.ValidationError;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationErrorTest {

    @Test
    void givenRandomObject_EqualsFalse() {
        ValidationError error = new ValidationError("field", "error");
        assertThat("random object", not(equalTo(error)));
    }

    @Test
    void exactMatchingFields() {
        ValidationError error = new ValidationError("field", "error");
        ValidationError sameError = new ValidationError("field", "error");

        ArrayList<ValidationError> errors = new ArrayList<>();
        errors.add(error);

        assertEquals(error, sameError);
        assertEquals(sameError, error);
        assertTrue(errors.contains(error));
        assertTrue(errors.contains(sameError));
    }
}
