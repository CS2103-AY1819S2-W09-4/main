package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.consultation.Assessment;
import seedu.address.model.consultation.Diagnosis;
import seedu.address.model.consultation.Symptom;
import seedu.address.model.patient.Address;
import seedu.address.model.patient.Contact;
import seedu.address.model.patient.Dob;
import seedu.address.model.patient.Email;
import seedu.address.model.patient.Gender;
import seedu.address.model.patient.Name;
import seedu.address.model.patient.Nric;
import seedu.address.model.patient.Patient;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.Assert;

public class DiagnosePatientCommandTest {

    private ModelManager modelManager = new ModelManager();
    private final CommandHistory history = new CommandHistory();

    @Before
    public void init() {
        Name name = new Name("Peter Tan");
        Nric nric = new Nric("S9123456A");
        Email email = new Email("ptan@gmail.com");
        Address address = new Address("1 Simei Road");
        Contact contact = new Contact("91111111");
        Gender gender = new Gender("M");
        Dob dob = new Dob("1991-01-01");
        ArrayList<Tag> tagList = new ArrayList<Tag>();
        Patient patient1 = new Patient(name, nric, email, address, contact, gender, dob, tagList);
        modelManager.addPatient(patient1);
    }

    @Test
    public void diagnosePatient() {
        // no consultation session
        String input = " a/migrane s/constant headache s/blurred vision";
        Assessment assessment = new Assessment("migrane");
        ArrayList<Symptom> symptoms = new ArrayList<>();
        symptoms.add(new Symptom("constant headache"));
        symptoms.add(new Symptom("blurred vision"));

        Assert.assertThrows(IllegalArgumentException.class, () ->
                modelManager.diagnosePatient(new Diagnosis(assessment, symptoms)));
    }

    @Test
    public void executeTest() {
        String userInput = "diagnose a/migrane s/constant headache s/blurred vision";
        Assessment assessment = new Assessment("migrane");
        ArrayList<Symptom> symptoms = new ArrayList<>();
        symptoms.add(new Symptom("constant headache"));
        symptoms.add(new Symptom("blurred vision"));

        Diagnosis diagnosis = new Diagnosis(assessment, symptoms);
        DiagnosePatientCommand command = new DiagnosePatientCommand(new Diagnosis(assessment, symptoms));

        modelManager.createConsultation(modelManager.getPatientAtIndex(1));

        try {
            assertEquals(command.execute(modelManager, history).getFeedbackToUser(), diagnosis.toString());
        } catch (CommandException ce) {
            org.junit.Assert.fail();
        }


    }
}
