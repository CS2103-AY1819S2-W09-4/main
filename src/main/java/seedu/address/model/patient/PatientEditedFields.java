package seedu.address.model.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import seedu.address.model.tag.Tag;

/**
 * Stores changes to patient records
 */
public class PatientEditedFields {

    private Name name;
    private Nric nric;
    private Email email;
    private Address address;
    private Contact contact;
    private Gender gender;
    private Dob dob;
    private ArrayList<Tag> tagList;

    public Optional<Name> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Optional<Nric> getNric() {
        return Optional.ofNullable(nric);
    }

    public void setNric(Nric nric) {
        this.nric = nric;
    }

    public Optional<Email> getEmail() {
        return Optional.ofNullable(email);
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Optional<Contact> getContact() {
        return Optional.ofNullable(contact);
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Optional<Gender> getGender() {
        return Optional.ofNullable(gender);
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Optional<Dob> getDob() {
        return Optional.ofNullable(dob);
    }

    public void setDob(Dob dob) {
        this.dob = dob;
    }

    public Optional<ArrayList<Tag>> getTagList() {
        return Optional.ofNullable(tagList);
    }

    public void setTagList(ArrayList<Tag> tagList) {
        this.tagList = tagList;
    }

    /**
     * Checks if PatientEditedFields is empty
     */
    public boolean checkEmpty() {
        return name == null && nric == null && email == null && address == null
                && contact == null && gender == null && dob == null && tagList == null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PatientEditedFields)) {
            return false;
        }

        PatientEditedFields otherPatient = (PatientEditedFields) other;
        return checkNonNullFields((PatientEditedFields) other);
    }

    /**
     * Only check the non null fields, and see if they are the same for two
     * PatientEditedFields
     */
    public boolean checkNonNullFields(PatientEditedFields other) {

        if ((name != null && other.getName() != null) && name.equals(other.getName())) {
            return false;
        }

        if ((nric != null && other.getNric() != null) && nric.equals(other.getNric())) {
            return false;
        }

        if ((email != null && other.getEmail() != null) && email.equals(other.getEmail())) {
            return false;
        }

        if ((address != null && other.getAddress() != null) && address.equals(other.getAddress())) {
            return false;
        }

        if ((contact != null && other.getContact() != null) && contact.equals(other.getContact())) {
            return false;
        }

        if ((gender != null && other.getGender() != null) && gender.equals(other.getGender())) {
            return false;
        }

        if ((dob != null && other.getDob() != null) && dob.equals(other.getDob())) {
            return false;
        }

        if (tagList != null && other.getTagList() != null) {
            if (!Arrays.equals(tagList.toArray(), other.getTagList().get().toArray())) {
                return false;
            }
        }

        return true;
    }

}
