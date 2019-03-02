package seedu.address.model.consultation;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import seedu.address.model.patient.Address;
import seedu.address.model.patient.Contact;
import seedu.address.model.patient.Dob;
import seedu.address.model.patient.Email;
import seedu.address.model.patient.Gender;
import seedu.address.model.patient.Name;
import seedu.address.model.patient.Nric;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.PatientManager;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.Assert;

public class ConsultationManagerTest {

    private static ConsultationManager consultationManager;
    private static PatientManager patientManager;

    @Before
    public void init() {
        this.consultationManager = new ConsultationManager();
        this.patientManager = new PatientManager();

        Name name = new Name("Peter Tan");
        Nric nric = new Nric("S9123456A");
        Email email = new Email("ptan@gmail.com");
        Address address = new Address("1 Simei Road");
        Contact contact = new Contact("91111111");
        Gender gender = new Gender("M");
        Dob dob = new Dob("1991-01-01");
        ArrayList<Tag> tagList = new ArrayList<Tag>();
        Patient patient1 = new Patient(name, nric, email, address, contact, gender, dob, tagList);

        patientManager.addPatient(patient1);
    }

    @Test
    public void createConsultation() {
        Patient patient = patientManager.getPatientAtIndex(1);
        consultationManager.createConsultation(patient);
        //attempt to start another consultation when there is an existing current session
        Assert.assertThrows(IllegalArgumentException.class, () -> consultationManager.createConsultation(patient));
    }

    @Test
    public void diagnosePatient() {

        // will only fail when diagnosis is created before a consultation begun

        ArrayList<Symptom>symptoms = new ArrayList<>();
        symptoms.add(new Symptom("Runny nose"));
        Assessment assessment = new Assessment("Cold");
        Diagnosis diagnosis = new Diagnosis(assessment, symptoms);

        Assert.assertThrows(IllegalArgumentException.class, () -> consultationManager.diagnosePatient(diagnosis));
    }

}
