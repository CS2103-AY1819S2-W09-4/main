package seedu.address.model.patient;

/**
 *  Represents the list of medicine prescribed for each patient
 */
public class Prescription {
    private String drug;
    private int quantity;

    public Prescription(String drug, int quantity) {
        this.drug = drug;
        this.quantity = quantity;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
