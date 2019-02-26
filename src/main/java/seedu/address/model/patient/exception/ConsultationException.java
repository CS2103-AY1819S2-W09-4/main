package seedu.address.model.patient.exception;

/**
 *  Exceptions pertaining to the diagnosis and prescription of drugs
 *  during a consultation session
 */
public class ConsultationException extends RuntimeException {
    public ConsultationException(String msg) {
        super(msg);
    }

    @Override
    public String toString() {
        return super.getMessage();
    }
}
