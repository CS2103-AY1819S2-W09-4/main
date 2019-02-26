package seedu.address.model.patient.exception;

/**
 *  Exceptions pertaining to patient creation, retrieval or update
 */
public class PatientException extends RuntimeException {
    public PatientException(String msg) {
        super(msg);
    }

    @Override
    public String toString() {
        return super.getMessage();
    }
}
