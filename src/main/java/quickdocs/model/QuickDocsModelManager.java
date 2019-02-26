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

    public void setCurrentSession(Consultation currentSession) {
        this.currentSession = currentSession;
    }

    public Patient getPatientByNric(String nric) {
        for (int i = 0; i < patientList.size(); i++) {
            if (patientList.get(i).getNric().equals(nric)) {
                return patientList.get(i);
            }
        }
        return null;
    }

    public void endConsultation() {
        currentSession = null;
    }

    public ArrayList<Consultation> getConsultationByNric(String nric) {

        ArrayList<Consultation> patientConsultations = new ArrayList<>();

        for (Consultation consultation: consultationList) {
            if (consultation.getPatient().getNric().equals(nric)) {
                patientConsultations.add(consultation);
            }
        }

        return patientConsultations;
    }

}
