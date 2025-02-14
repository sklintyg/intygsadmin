package se.inera.intyg.intygsadmin.web.service.status;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendNotificationRequestValidatorTest {

    @InjectMocks
    private SendNotificationRequestValidator sendNotificationRequestValidator;

    @Nested
    class ValidateId {

        @Test
        void shouldThrowExceptionIfIdIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> sendNotificationRequestValidator.validateId(""));
        }

        @Test
        void shouldThrowExceptionIfIdIsNull() {
            assertThrows(IllegalArgumentException.class, () -> sendNotificationRequestValidator.validateId(null));
        }

        @Test
        void shouldThrowExceptionIfIdIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> sendNotificationRequestValidator.validateId(" "));
        }

        @Test
        void shouldNotThrowIfValidId() {
            assertDoesNotThrow(() -> sendNotificationRequestValidator.validateId("ID"));
        }
    }

    @Nested
    class ValidateIds {

        @Test
        void shouldThrowExceptionIfIdIsEmpty() {
            final var ids = new ArrayList<String>();
            assertThrows(IllegalArgumentException.class, () -> sendNotificationRequestValidator.validateIds(ids));
        }

        @Test
        void shouldThrowExceptionIfIdIsNull() {
            assertThrows(IllegalArgumentException.class, () -> sendNotificationRequestValidator.validateIds(null));
        }

        @Test
        void shouldNotThrowIfValidId() {
            assertDoesNotThrow(() -> sendNotificationRequestValidator.validateIds(List.of("ID")));
        }
    }

    @Nested
    class ValidateDateWithoutIntervalLimit {

        @Test
        void shouldThrowExceptionIfStartDateIsBeforeLimit() {
            final var end = LocalDateTime.now().minusDays(4);
            final var start = LocalDateTime.now().minusDays(5);
            final var limitBack = 4;
            final var limitInterval = 10;

            assertThrows(IllegalArgumentException.class,
                () -> sendNotificationRequestValidator.validateDate(start, end, limitInterval, limitBack));
        }

        @Test
        void shouldNotThrowExceptionIfStartDateIsAfterLimit() {
            final var end = LocalDateTime.now().minusDays(1);
            final var start = LocalDateTime.now().minusDays(2);
            final var limitBack = 4;
            final var limitInterval = 10;

            assertDoesNotThrow(() -> sendNotificationRequestValidator.validateDate(start, end, limitInterval, limitBack));
        }

        @Test
        void shouldThrowExceptionIfEndIsAfterStart() {
            final var end = LocalDateTime.now().minusDays(1);
            final var start = LocalDateTime.now();
            final var limitBack = 4;
            final var limitInterval = 10;

            assertThrows(IllegalArgumentException.class,
                () -> sendNotificationRequestValidator.validateDate(start, end, limitInterval, limitBack));
        }

        @Test
        void shouldThrowExceptionIfIntervalIsOverLimit() {
            final var end = LocalDateTime.now();
            final var start = LocalDateTime.now().minusDays(11);
            final var limitBack = 20;
            final var limitInterval = 10;

            assertThrows(IllegalArgumentException.class,
                () -> sendNotificationRequestValidator.validateDate(start, end, limitInterval, limitBack));
        }

        @Test
        void shouldThrowExceptionIfIntervalIsOverLimitWhenEndIsNull() {
            final var start = LocalDateTime.now().minusDays(11);
            final var limitBack = 20;
            final var limitInterval = 10;

            assertThrows(IllegalArgumentException.class,
                () -> sendNotificationRequestValidator.validateDate(start, null, limitInterval, limitBack));
        }
    }

    @Nested
    class ValidateDateWithIntervalLimit {

        @Test
        void shouldThrowExceptionIfStartDateIsBeforeLimit() {
            final var end = LocalDateTime.now().minusDays(4);
            final var start = LocalDateTime.now().minusDays(5);
            final var limit = 4;

            assertThrows(IllegalArgumentException.class, () -> sendNotificationRequestValidator.validateDate(start, end, limit));
        }

        @Test
        void shouldThrowExceptionIfEndIsAfterStart() {
            final var end = LocalDateTime.now().minusDays(1);
            final var start = LocalDateTime.now();
            final var limit = 4;

            assertThrows(IllegalArgumentException.class, () -> sendNotificationRequestValidator.validateDate(start, end, limit));
        }

        @Test
        void shouldNotThrowExceptionIfStartDateIsAfterLimit() {
            final var end = LocalDateTime.now().minusDays(1);
            final var start = LocalDateTime.now().minusDays(2);
            final var limit = 4;

            assertDoesNotThrow(() -> sendNotificationRequestValidator.validateDate(start, end, limit));
        }
    }

}
