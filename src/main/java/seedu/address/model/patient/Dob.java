package seedu.address.model.patient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the patient's date of birth
 */
public class Dob {
    //public static final String REGEX_DOB = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/[0-9]{4}";
    public static final String REGEX_DOB = "[0-9]{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";
    public static final String DOB_CONSTRAINTS = "Date of Birth should be in YYYY-MM-DD format";
    public static final String FEBURARY_CONSTRAINT = "Feburary only have 28 or 29 days";
    public static final String LEAPYEAR_CONSTRAINT = "Only leap years have 29 Feb";
    public static final String THIRTYFIRST_CONSTRAINT = "The month entered does not have a 31st day";

    private LocalDate dob;

    public Dob() {
    }

    public Dob(String dob) {
        if (!dob.matches(REGEX_DOB)) {
            throw new IllegalArgumentException(DOB_CONSTRAINTS);
        }

        String[] splittedDob = dob.split("-");
        boolean isLeapYear = false;
        String month = splittedDob[1];
        String day = splittedDob[2];
        int year = Integer.valueOf(splittedDob[0]);
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            isLeapYear = true;
        }

        if (!isLeapYear && month.equals("2") && Integer.valueOf(day) > 28) {
            throw new IllegalArgumentException(LEAPYEAR_CONSTRAINT);
        }

        if (Integer.valueOf(month) == 2 && Integer.valueOf(day) > 29) {
            throw new IllegalArgumentException(FEBURARY_CONSTRAINT);
        }

        List<Integer> monthsWith31Days = Arrays.asList(1, 3, 5, 7, 8, 10, 12);
        if (!monthsWith31Days.contains(Integer.valueOf(month)) && Integer.valueOf(day) == 31) {
            throw new IllegalArgumentException(THIRTYFIRST_CONSTRAINT);
        }

        this.dob = LocalDate.parse(dob);
    }

    public LocalDate getDob() {
        return dob;
    }

    @Override
    public String toString() {
        return dob.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Dob // instanceof handles nulls
                && dob.toString().equals(((Dob) other).getDob())); // state check
    }
}
