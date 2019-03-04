package seedu.address.logic.commands;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import jdk.nashorn.api.tree.CatchTree;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.consultation.Assessment;
import seedu.address.model.consultation.Prescription;
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

public class PrescriptionCommandTest {

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
    public void executeTest() {
        // no consultation
        ArrayList<String> medList = new ArrayList<>();
        ArrayList<Integer> qtyList = new ArrayList<>();
        medList.add("antibiotics");
        qtyList.add(1);

        PrescriptionCommand prescriptionCommand;

        try {
            prescriptionCommand = new PrescriptionCommand(medList, qtyList);

        } catch (Exception ex) {
            org.junit.Assert.assertEquals(ex.toString(),
                    "There is no ongoing consultation to prescribe medicine to");
        }

        modelManager.createConsultation(modelManager.getPatientAtIndex(1));
        try {
            prescriptionCommand = new PrescriptionCommand(medList, qtyList);
            StringBuilder sb = new StringBuilder();
            sb.append("prescription:\n");
            sb.append("==============================\n");
            sb.append(new Prescription(medList.get(0), qtyList.get(0)));
            org.junit.Assert.assertEquals(prescriptionCommand.execute(modelManager, history).getFeedbackToUser(),
                    sb.toString());
        } catch (Exception ex) {
            org.junit.Assert.fail();
        }


    }

}
