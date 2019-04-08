package quickdocs.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import quickdocs.logic.CommandHistory;
import quickdocs.logic.parser.AddAppCommandParser;
import quickdocs.model.appointment.Appointment;
import quickdocs.model.appointment.AppointmentManager;
import quickdocs.logic.commands.exceptions.CommandException;
import quickdocs.model.Model;
import quickdocs.model.patient.Nric;
import quickdocs.model.patient.Patient;

/**
 * Adds an appointment to quickdocs.
 */
public class AddAppCommand extends Command {

    public static final String COMMAND_WORD = "addapp";
    public static final String COMMAND_ALIAS = "aa";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment to quickdocs. "
            + "Parameters: "
            + AddAppCommandParser.PREFIX_NRIC + "NRIC "
            + AddAppCommandParser.PREFIX_DATE + "DATE "
            + AddAppCommandParser.PREFIX_START + "START "
            + AddAppCommandParser.PREFIX_END + "END "
            + AddAppCommandParser.PREFIX_COMMENT + "COMMENT\n"
            + "Example: " + COMMAND_WORD + " "
            + AddAppCommandParser.PREFIX_NRIC + "S9625555I "
            + AddAppCommandParser.PREFIX_DATE + "2019-10-23 "
            + AddAppCommandParser.PREFIX_START + "16:00 "
            + AddAppCommandParser.PREFIX_END + "17:00 "
            + AddAppCommandParser.PREFIX_COMMENT + "<any comments>\n";

    public static final String MESSAGE_SUCCESS = "Appointment added:\n%1$s\n";
    public static final String MESSAGE_CONFLICTING_APP = "There are clashes in the time slot chosen."
            + " Please use 'freeapp' to find free slots.";
    public static final String MESSAGE_PATIENT_NOT_FOUND = "No patient with the given nric found";
    public static final String MESSAGE_START_EQUALS_END = "Appointment start time and end time should not be the same.";
    public static final String MESSAGE_START_AFTER_END = "Appointment start time should not be after end time.";
    public static final String MESSAGE_NON_OFFICE_HOURS = "Appointment timing is outside of office hours.";

    private final Nric nric;
    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;
    private final String comment;

    /**
     * Creates an AddAppCommand to add the specified {@code Appointment}
     */
    public AddAppCommand(Nric nric, LocalDate date, LocalTime start, LocalTime end, String comment) {
        this.nric = nric;
        this.date = date;
        this.start = start;
        this.end = end;
        this.comment = comment;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        Optional<Patient> patientToAdd = model.getPatientByNric(nric);
        if (!patientToAdd.isPresent()) {
            throw new CommandException(MESSAGE_PATIENT_NOT_FOUND);
        }

        if (start.isBefore(AppointmentManager.OPENING_HOUR) || start.isAfter(AppointmentManager.CLOSING_HOUR)
                || end.isBefore(AppointmentManager.OPENING_HOUR) || end.isAfter(AppointmentManager.CLOSING_HOUR)) {
            throw new CommandException(MESSAGE_NON_OFFICE_HOURS);
        }

        if (start.equals(end)) {
            throw new CommandException(MESSAGE_START_EQUALS_END);
        }

        if (start.isAfter(end)) {
            throw new CommandException(MESSAGE_START_AFTER_END);
        }

        Appointment appToAdd = new Appointment(patientToAdd.get(), date, start, end, comment);
        if (model.hasTimeConflicts(appToAdd)) {
            throw new CommandException(MESSAGE_CONFLICTING_APP);
        }

        model.addApp(appToAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, appToAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddAppCommand // instanceof handles nulls
                && nric.equals(((AddAppCommand) other).nric));
    }
}
