package seedu.address.model.patient;

import java.util.ArrayList;
import java.util.List;

/**
 * Consultation consist of the reference to the patient record
 * and also the diagnosis assessed by the doctor, with the
 * list of prescribed drugs
 */
public class Consultation {
    private int index;
    private Patient patient;
    private Diagnosis diagnosis;
    private List<Prescription> prescriptions;

    public Consultation(Patient patient) {
        this.index = -1;
        this.patient = patient;
        diagnosis = new Diagnosis();
        prescriptions = new ArrayList<Prescription>();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
