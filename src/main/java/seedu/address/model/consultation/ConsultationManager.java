package seedu.address.model.consultation;

import java.util.ArrayList;

import seedu.address.model.patient.Patient;

/**
 * Handle all model operations concerning with consultations
 */
public class ConsultationManager {

    private ArrayList<Consultation> consultationList;
    private Consultation currentConsultation;

    public ConsultationManager() {
        this.consultationList = new ArrayList<Consultation>();
    }

    // add diagnosis methods

    /**
     * Check if there is an ongoing consultation session
     */
    public boolean checkConsultation() {
        return currentConsultation != null;
    }

    /**
     * Create a consultation session with the patient indicated
     */
    public void createConsultation(Patient patient) {
        if (currentConsultation != null) {
            throw new IllegalArgumentException("There is already an ongoing consultation session");
        }
        currentConsultation = new Consultation(patient);
    }

    /**
     * Adds or replace diagnosis on current consultation session
     */
    public void diagnosePatient(Diagnosis diagnosis) {
        if (currentConsultation == null) {
            throw new IllegalArgumentException("There is no ongoing consultation session");
        }
        currentConsultation.setDiagnosis(diagnosis);
    }

    public void prescribeMedicine(ArrayList<Prescription> prescriptions) {
        currentConsultation.setPrescriptions(prescriptions);
    }

    public Consultation getCurrentConsultation() {
        return currentConsultation;
    }

    public ArrayList<Consultation> getConsultationList() {
        return consultationList;
    }

    /**
     * End the current consultation and store it into the list
     */
    public void endConsultation() {
        currentConsultation.setIndex(consultationList.size());
        consultationList.add(currentConsultation);
        currentConsultation = null;
    }

    /**
     * List consultation by name or nric
     */
    public ArrayList<Consultation> listConsultation(String value) {

        ArrayList<Consultation> consultationsFound = new ArrayList<>();

        for (Consultation consult : consultationList) {
            if (consult.getPatient().getNric().getNric().equals(value)) {
                consultationsFound.add(consult);
            }
        }
        return consultationsFound;
    }

    /**
     * return consultation session based on index
     */
    public Consultation listConsultation(int index) {
        return consultationList.get(index - 1);
    }

}
