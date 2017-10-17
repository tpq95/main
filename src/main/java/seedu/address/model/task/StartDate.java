package seedu.address.model.task;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Optional;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents the Starting Date of a given task in the application.
 * Guarantees: immutable; is valid as declared in {@link TaskDates#formatDate(String)}
 */
public class StartDate extends TaskDates {

    public final LocalDate date;

    /**
     * Validates given starting date.
     *
     * @throws IllegalValueException if given date string is invalid.
     */
    public StartDate(String date) throws IllegalValueException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        //this.date = date.isEmpty() ? Optional.empty() : Optional.of(TaskDates.formatDate(trimmedDate));
        this.date = TaskDates.formatDate(date);
    }

    @Override
    public String toString() {
        return date.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StartDate // instanceof handles nulls
                && this.date.equals(((StartDate) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

}
