package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.util.SamplePatientsUtil.getSamplePatients;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.patient.Nric;
import seedu.address.model.patient.Patient;

public class AddAppCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(new AddressBook(), new UserPrefs(), getSamplePatients());
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void init() {
        Nric nric = new Nric("S9234568C");
        LocalDate date = LocalDate.parse("2019-10-23");
        LocalTime start = LocalTime.parse("16:00");
        LocalTime end = LocalTime.parse("17:00");
        String comment = "This is a comment";
        Patient patientToAdd = model.getPatientWithNric(nric);
        Appointment toAdd = new Appointment(patientToAdd, date, start, end, comment);

        model.addApp(toAdd);
    }

    @Test
    public void executeValidAddAppointment() throws Exception {
        Nric nric = new Nric("S9234567B");
        LocalDate date = LocalDate.parse("2019-10-23");
        LocalTime start = LocalTime.parse("16:00");
        LocalTime end = LocalTime.parse("17:00");
        String comment = "This is a comment";

        CommandResult commandResult = new AddAppCommand(nric, date, start, end, comment)
                .execute(model, commandHistory);
        Patient patientToAdd = model.getPatientWithNric(nric);
        Appointment toAdd = new Appointment(patientToAdd, date, start, end, comment);

        StringBuilder sb = new StringBuilder();
        sb.append("Appointment added:\n")
                .append(toAdd.toString() + "\n");

        Assert.assertEquals(sb.toString(), commandResult.getFeedbackToUser());

    }

    @Test
    public void executeDuplicateAddAppointment() throws Exception {
        Nric nric = new Nric("S9234568C");
        LocalDate date = LocalDate.parse("2019-10-23");
        LocalTime start = LocalTime.parse("16:00");
        LocalTime end = LocalTime.parse("17:00");
        String comment = "This is a comment";
        AddAppCommand addAppCommand = new AddAppCommand(nric, date, start, end, comment);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddAppCommand.MESSAGE_DUPLICATE_APP);
        addAppCommand.execute(model, commandHistory);
    }

    @Test
    public void equals() {
        Nric nricA = new Nric("S9234568C");
        Nric nricB = new Nric("S9234567B");
        LocalDate date = LocalDate.parse("2019-10-23");
        LocalTime start = LocalTime.parse("16:00");
        LocalTime end = LocalTime.parse("17:00");
        String comment = "This is a comment";

        AddAppCommand addAppA = new AddAppCommand(nricA, date, start, end, comment);
        AddAppCommand addAppB = new AddAppCommand(nricB, date, start, end, comment);

        // same object -> returns true
        assertTrue(addAppA.equals(addAppA));

        // same values -> returns true
        AddAppCommand addAppACopy = new AddAppCommand(nricA, date, start, end, comment);
        assertTrue(addAppA.equals(addAppACopy));

        // different types -> returns false
        assertFalse(addAppA.equals(1));

        // null -> returns false
        assertFalse(addAppA.equals(null));

        // different person -> returns false
        assertFalse(addAppA.equals(addAppB));
    }
}
