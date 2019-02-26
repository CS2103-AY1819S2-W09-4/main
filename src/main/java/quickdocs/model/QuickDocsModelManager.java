package quickdocs.model;

import java.util.ArrayList;

import seedu.address.model.patient.Consultation;
import seedu.address.model.patient.Patient;

/**
 * Represent in-memory model of QuickDocs
 */
public class QuickDocsModelManager {

    private final ArrayList<Patient> patientList;
    private final ArrayList<Consultation> consultationList;

    // the current consultation session
    private Consultation currentSession;

    public QuickDocsModelManager() {
        this.patientList = new ArrayList<Patient>();
        this.consultationList = new ArrayList<Consultation>();
    }

    // patient and consultation methods

    public ArrayList<Patient> getPatientList() {
        return patientList;
    }

    public ArrayList<Consultation> getConsultationList() {
        return consultationList;
    }

    public Consultation getCurrentSession() {
        return currentSession;
    }


}
