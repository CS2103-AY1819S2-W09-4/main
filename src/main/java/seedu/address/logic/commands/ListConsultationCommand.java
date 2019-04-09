package seedu.address.logic.commands;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.consultation.Consultation;

/**
 * List previous consultation sessions of a single patient if the NRIC of the patient
 * is supplied. If the index is supplied instead, then display the details of the
 * specific consultation session
 */
public class ListConsultationCommand extends Command {

    public static final String COMMAND_WORD = "listconsult";
    public static final String COMMAND_ALIAS = "lc";
    public static final String NO_RECORDS = "No past consultation records found.\n";
    public static final String INVALID_INDEX = "Index entered is invalid.\n";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": list all past consultation records of a single patient using patient's NRIC "
            + "or details of a single consultation record through its index.\n"
            + "Parameters: "
            + "INDEX OR NRIC\n"
            + "Example: " + COMMAND_WORD + " r/S9237161A\n"
            + "or: " + COMMAND_WORD + " 10\n";

    private int index;
    private String nric;

    private int constructedBy;

    public ListConsultationCommand(int index) {
        this.index = index;
        constructedBy = 1;
    }

    public ListConsultationCommand(String value) {
        this.nric = value;
        constructedBy = 2;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {

        if (constructedBy == 1 && (index > model.getConsultationList().size() || index < 1)) {
            throw new CommandException(INVALID_INDEX);
        }

        if (constructedBy == 1) {
            return new CommandResult(model.listConsultation(index).toString());
        }

        ArrayList<Consultation> consultations = model.listConsultation(nric);
        String result = listingConsultations(consultations);

        return new CommandResult(result);
    }

    /**
     * List all the past consultation records of a single patient
     */
    public String listingConsultations(ArrayList<Consultation> consultations) {
        StringBuilder sb = new StringBuilder();
        sb.append("Listing consultation records\n");
        sb.append("====================\n");

        if (consultations.isEmpty()) {
            sb.append(NO_RECORDS);
            return sb.toString();
        }

        for (Consultation con : consultations) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            sb.append(1 + con.getIndex() + ") ");
            sb.append(" visit for " + con.getDiagnosis().getAssessment() + " ");
            sb.append(" on " + con.getSession().format(formatter));
            sb.append("\n");
        }

        return sb.toString();
    }

    public int getIndex() {
        return index;
    }

    public String getNric() {
        return nric;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ListConsultationCommand
                && (getIndex() == ((ListConsultationCommand) other).getIndex())
                || getNric().equals(((ListConsultationCommand) other).getNric()));
    }

}
