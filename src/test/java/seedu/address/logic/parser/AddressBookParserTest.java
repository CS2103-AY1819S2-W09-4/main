package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddAppCommand;
import seedu.address.logic.commands.AddPatientCommand;
import seedu.address.logic.commands.AddRemCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ConsultationCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DiagnosePatientCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditPatientCommand;
import seedu.address.logic.commands.EndConsultationCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListAppCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListPatientCommand;
import seedu.address.logic.commands.ListRemCommand;
import seedu.address.logic.commands.PrescriptionCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
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
import seedu.address.model.patient.PatientEditedFields;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            throw new AssertionError("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }

    @Test
    public void parseCommand_addPatient() throws Exception {
        Name name = new Name("Peter Tan");
        Nric nric = new Nric("S9123456A");
        Email email = new Email("ptan@gmail.com");
        Address address = new Address("1 Simei Road");
        Contact contact = new Contact("91111111");
        Gender gender = new Gender("M");
        Dob dob = new Dob("1991-01-01");
        ArrayList<Tag> tagList = new ArrayList<Tag>();
        Patient patient1 = new Patient(name, nric, email, address, contact, gender, dob, tagList);

        String userInput = "padd n/" + name.getName() + " "
                + "r/" + nric.getNric() + " "
                + "e/" + email.getEmail() + " "
                + "a/" + address.getAddress() + " "
                + "c/" + contact.getContact() + " "
                + "g/" + gender.getGender() + " "
                + "d/" + dob.getDob();

        AddPatientCommand command = (AddPatientCommand) parser.parseCommand(userInput);

        assertEquals(new AddPatientCommand(patient1), command);
    }

    @Test
    public void parseCommand_editPatient() throws Exception {
        Name name = new Name("Peter Tan");
        Nric nric = new Nric("S9123456A");
        Email email = new Email("ptan@gmail.com");
        Address address = new Address("1 Simei Road");

        String userInput = "pedit 1 n/" + name.getName() + " "
                + "r/" + nric.getNric() + " "
                + "e/" + email.getEmail() + " "
                + "a/" + address.getAddress();

        PatientEditedFields pef = new PatientEditedFields();
        pef.setName(name);
        pef.setNric(nric);
        pef.setEmail(email);
        pef.setAddress(address);

        EditPatientCommand expectedCommand = new EditPatientCommand(1, pef);

        EditPatientCommand command = (EditPatientCommand) parser.parseCommand(userInput);
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parseCommand_listPatient() throws Exception {
        String userInput = "plist r/S92";
        ListPatientCommand command1 = new ListPatientCommand("S92", false);
        assertEquals(command1, parser.parseCommand(userInput));

        userInput = "plist n/pe";
        ListPatientCommand command2 = new ListPatientCommand("pe", true);
        assertEquals(command2, parser.parseCommand(userInput));

        userInput = "plist t/diabetes";
        ListPatientCommand command3 = new ListPatientCommand(new Tag("diabetes"));
        assertEquals(command3, parser.parseCommand(userInput));

        userInput = "plist 1";
        ListPatientCommand command4 = new ListPatientCommand(1);
        assertEquals(command4, parser.parseCommand(userInput));

        userInput = "plist";
        ListPatientCommand command5 = new ListPatientCommand();
        assertEquals(command5, parser.parseCommand(userInput));
    }

    @Test
    public void parseCommand_consultationcommand() throws Exception {
        String userInput = "consult r/S9123456A";
        ConsultationCommand command = new ConsultationCommand("S9123456A");
        assertEquals(command, parser.parseCommand(userInput));
    }

    @Test
    public void parseCommand_diagnosepatientcommand() throws Exception {
        String userInput = "diagnose a/migrane s/constant headache s/blurred vision";
        Assessment assessment = new Assessment("migrane");
        ArrayList<Symptom> symptoms = new ArrayList<>();
        symptoms.add(new Symptom("constant headache"));
        symptoms.add(new Symptom("blurred vision"));
        DiagnosePatientCommand command = new DiagnosePatientCommand(new Diagnosis(assessment, symptoms));
        assertEquals(command, parser.parseCommand(userInput));
    }

    @Test
    public void parseCommand_prescriptioncommand() throws Exception {
        String userInput = "prescribe m/antibiotics q/1";
        ArrayList<String> meds = new ArrayList<>();
        meds.add("antibiotics");
        ArrayList<Integer> qtys = new ArrayList<>();
        qtys.add(1);
        PrescriptionCommand command = new PrescriptionCommand(meds, qtys);
        assertEquals(command, parser.parseCommand(userInput));

    }

    @Test
    public void parseCommand_endconsultation() throws Exception {
        String userInput = "endconsult";
        org.junit.Assert.assertTrue(parser.parseCommand(userInput) instanceof EndConsultationCommand);
    }

    @Test
    public void parseCommand_addAppointment() throws Exception {
        String nricString = "S9234568C";
        String dateString = "2019-10-23";
        String startString = "16:00";
        String endString = "17:00";

        Nric nric = new Nric(nricString);
        LocalDate date = LocalDate.parse(dateString);
        LocalTime start = LocalTime.parse(startString);
        LocalTime end = LocalTime.parse(endString);
        String comment = "This is a test comment";


        String userInput = AddAppCommand.COMMAND_WORD
                + " r/" + nricString
                + " d/" + dateString
                + " s/" + startString
                + " e/" + endString
                + " c/" + comment;
        AddAppCommand command = (AddAppCommand) parser.parseCommand(userInput);
        assertEquals(new AddAppCommand(nric, date, start, end, comment), command);
    }

    @Test
    public void parseCommand_listApp() throws Exception {
        assertTrue(parser.parseCommand(ListAppCommand.COMMAND_WORD) instanceof ListAppCommand);
        assertTrue(parser.parseCommand(ListAppCommand.COMMAND_WORD + " 3") instanceof ListAppCommand);
    }

    @Test
    public void parseCommand_addReminder() throws Exception {
        String title = "Refill Medicine ABC";
        String dateString = "2019-05-22";
        String startString = "13:00";
        String endString = "14:00";
        String comment = "This is a test comment";

        LocalDate date = LocalDate.parse(dateString);
        LocalTime start = LocalTime.parse(startString);
        LocalTime end = LocalTime.parse(endString);
        Reminder toAdd = new Reminder(title, comment, date, start, end);

        String userInput = AddRemCommand.COMMAND_WORD
                + " t/" + title
                + " d/" + dateString
                + " s/" + startString
                + " e/" + endString
                + " c/" + comment;
        AddRemCommand command = (AddRemCommand) parser.parseCommand(userInput);
        assertEquals(new AddRemCommand(toAdd), command);
    }

    @Test
    public void parseCommand_listReminder() throws Exception {
        assertTrue(parser.parseCommand(ListRemCommand.COMMAND_WORD) instanceof ListRemCommand);
        assertTrue(parser.parseCommand(ListRemCommand.COMMAND_WORD + " 3") instanceof ListRemCommand);
    }
}
