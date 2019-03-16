package seedu.address.model.patient;

/**
 * Represents the full name of the person in the patient record
 */
public class Name {

    public static final String REGEX_NAME = "[\\p{Alnum}][\\p{Alnum} ]*";
    public static final String NAME_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces but not blank";

    private String name;

    public Name() {
    }

    public Name (String name) {
        if (!name.matches(REGEX_NAME)) {
            throw new IllegalArgumentException(NAME_CONSTRAINTS);
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Name // instanceof handles nulls
                && name.equals(((Name) other).getName())); // state check
    }


}
