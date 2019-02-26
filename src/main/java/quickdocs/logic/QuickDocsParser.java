package quickdocs.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import quickdocs.model.QuickDocsModelManager;
import seedu.address.logic.commands.patient.ConsultationCommands;
import seedu.address.logic.commands.patient.PatientCommands;
import seedu.address.model.patient.exception.PatientException;

/**
 *  QuickDocsParser will take in the commands the user entered
 *  and handles the various operations involving the different modules
 */

public class QuickDocsParser {

    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private QuickDocsModelManager modelManager;

    private PatientCommands patientCommands;
    private ConsultationCommands consultationCommands;

    /**
     * For every command, need to set model manager to access the data
     * @param modelManager consisting of the list of patient, consultation, appointment, medicine data
     */
    public QuickDocsParser(QuickDocsModelManager modelManager) {
        this.modelManager = modelManager;
        // for patient and consultation commands
        this.patientCommands = new PatientCommands();
        this.patientCommands.setModelManager(modelManager);
        this.consultationCommands = new ConsultationCommands();
        this.consultationCommands.setModelManager(modelManager);
    }

    public String parseCommand(String userInput) throws PatientException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new PatientException("Wrong command format!");
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        switch (commandWord) {
        case "padd":
            return patientCommands.addPatient(arguments);
        case "pedit":
            return patientCommands.editPatient(arguments);
        case "plist" :
            return patientCommands.listPatient(arguments);
        case "consult" :
            return consultationCommands.addDiagnosis(arguments);
        case "dedit" :
            return consultationCommands.editDiagnosis(arguments);
        case "prescribe" :
            return consultationCommands.addPrescription(arguments);
        case "endconsult" :
            return consultationCommands.endConsultation();
        default:
            throw new PatientException("No command found.");
        }
    }
}
