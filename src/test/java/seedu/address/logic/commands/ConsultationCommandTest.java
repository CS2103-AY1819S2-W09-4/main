package seedu.address.logic.commands;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
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

public class ConsultationCommandTest {

    private Model modelManager = new ModelManager();
    //private ModelManager modelManager = new ModelManager();
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
    public void createConsultation() {

        modelManager.createConsultation(modelManager.getPatientAtIndex(1));

        // command exception thrown when consultation is recreated with a ongoing session
        Assert.assertThrows(IllegalArgumentException.class, () ->
                modelManager.createConsultation(modelManager.getPatientAtIndex(1)));
    }

    @Test
    public void executeTest() {
        ConsultationCommand cr = new ConsultationCommand("S9123456B");
        Assert.assertThrows(CommandException.class, ()->cr.execute(modelManager, history));

        ConsultationCommand cr2 = new ConsultationCommand("S9123456A");
        try {
            String consultationResult = "Consultation session for: " + "S9123456A" + " started";
            org.junit.Assert.assertEquals(cr2.execute(modelManager, history),
                    new CommandResult(consultationResult));
        } catch (CommandException ce) {
            org.junit.Assert.fail();
        }
    }


}
