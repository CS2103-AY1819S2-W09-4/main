package seedu.address.model.patient;

import java.util.List;

/**
 * Represents the diagnosis of each consultation session
 */
public class Diagnosis {

    private String assessment;
    private List<String> Symptoms;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public List<String> getSymptoms() {
        return Symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        Symptoms = symptoms;
    }
}
