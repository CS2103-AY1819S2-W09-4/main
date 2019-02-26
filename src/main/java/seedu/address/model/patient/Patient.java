package seedu.address.model.patient;

import java.util.Calendar;
import java.util.List;

import seedu.address.model.tag.Tag;

/**
 *  Represents a single patient in QuickDocs
 */

public class Patient {
    private String name;
    private String nric;
    private Calendar dob;
    private String address;
    private String email;
    private int contact;
    private char gender;
    private List<Tag> tags;

    public Patient(String name, String nric, Calendar dob,
                   String address, String email, int contact, char gender, List<Tag> tags) {
        this.name = name;
        this.nric = nric;
        this.dob = dob;
        this.address = address;
        this.email = email;
        this.contact = contact;
        this.gender = gender;
        this.tags = tags;
    }

    public Patient (Patient otherPatient) {
        this.name = otherPatient.name;
        this.nric = otherPatient.nric;
        this.dob = otherPatient.dob;
        this.address = otherPatient.address;
        this.email = otherPatient.email;
        this.contact = otherPatient.contact;
        this.gender = otherPatient.gender;
        this.tags = otherPatient.tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public Calendar getDob() {
        return dob;
    }

    public void setDob(Calendar dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        StringBuilder sb = new StringBuilder();
        sb.append("Patient details for: " + nric + "\n");
        sb.append("Name: " + name + "\n");
        sb.append("Date of Birth: " + day + "-" + month + "-" + year + " \n");
        sb.append("Gender: " + gender + "\n");
        sb.append("Contact: " + contact + "\n");
        sb.append("Email: " + email + "\n");
        sb.append("Address: " + address + "\n");
        sb.append("Tags: " + tags.toString() + "\n");

        return sb.toString();
    }
}
