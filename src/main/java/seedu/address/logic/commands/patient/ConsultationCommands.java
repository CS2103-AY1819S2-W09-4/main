package seedu.address.logic.commands.patient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import quickdocs.model.QuickDocsModelManager;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.patient.Consultation;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.Prescription;
import seedu.address.model.patient.exception.ConsultationException;

/**
 * Consists of commands pertaining to the diagnosis and prescription of drugs during
 * each consultation session
 */
public class ConsultationCommands {

    public static final Prefix PREFIX_NRIC = new Prefix("r/");
    public static final Prefix PREFIX_ASSESSMENT = new Prefix("a/");
    public static final Prefix PREFIX_SYMPTOM = new Prefix("s/");
    public static final Prefix PREFIX_DRUG = new Prefix("d/");
    public static final Prefix PREFIX_QUANTITY = new Prefix("q/");

    private QuickDocsModelManager modelManager;

    public void setModelManager(QuickDocsModelManager modelManager)
    {
        this.modelManager = modelManager;
    }

    public String addDiagnosis(String args) {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NRIC, PREFIX_ASSESSMENT, PREFIX_SYMPTOM);

        if (!arePrefixesPresent(argMultimap, PREFIX_NRIC, PREFIX_ASSESSMENT, PREFIX_SYMPTOM)) {
            throw new ConsultationException("Some details are left out, please retype the command");
        }

        // currently only allow consultation via nric
        Patient currentPatient = modelManager.getPatientByNric(argMultimap.getValue(PREFIX_NRIC).get());
        if (currentPatient == null) {
            throw new ConsultationException("No patient found, please retype the command");
        }

        Consultation consultation = new Consultation(currentPatient);

        // prevent creating another consultation session until the current one is finished
        if (modelManager.getCurrentSession() != null) {
            throw new ConsultationException("Diagnosis is present, please edit diagnosis instead");
        }

        modelManager.setCurrentSession(consultation);
        consultation.getDiagnosis().setAssessment(argMultimap.getValue(PREFIX_ASSESSMENT).get());
        consultation.getDiagnosis().setSymptoms(parseSymptoms(argMultimap.getAllValues(PREFIX_SYMPTOM)));

        return formatDiagnosis() + "Diagnosis recorded for patient: " + currentPatient.getName() + "\n\n";
    }

    public String editDiagnosis(String args) {

        if (modelManager.getCurrentSession() == null) {
            throw new ConsultationException("No consultation session at the moment.");
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ASSESSMENT, PREFIX_SYMPTOM);

        if (!arePrefixesPresent(argMultimap, PREFIX_SYMPTOM)
                && !arePrefixesPresent(argMultimap, PREFIX_ASSESSMENT)) {
            throw new ConsultationException("Some details are left out, please retype the command");
        }
        Consultation consultation = modelManager.getCurrentSession();

        if (argMultimap.getValue(PREFIX_ASSESSMENT).isPresent()) {
            consultation.getDiagnosis().setAssessment(argMultimap.getValue(PREFIX_ASSESSMENT).get());
        }

        if (argMultimap.getValue(PREFIX_SYMPTOM).isPresent()) {
            consultation.getDiagnosis().setSymptoms(parseSymptoms(argMultimap.getAllValues(PREFIX_SYMPTOM)));
        }

        return formatDiagnosis() + "Diagnosis recorded for patient: "
                + consultation.getPatient().getName() + "\n\n";

    }

    public String addPrescription(String args) {
        if (modelManager.getCurrentSession() == null) {
            throw new ConsultationException("No consultation session at the moment.");
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DRUG, PREFIX_QUANTITY);

        if (!arePrefixesPresent(argMultimap, PREFIX_DRUG, PREFIX_QUANTITY)) {
            throw new ConsultationException("Some details are left out, please retype the command");
        }

        ArrayList<String>drugList = (ArrayList<String>) argMultimap.getAllValues(PREFIX_DRUG);
        ArrayList<String>qtyList = (ArrayList<String>) argMultimap.getAllValues(PREFIX_QUANTITY);

        if (drugList.size() != qtyList.size()) {
            return "Some medicine do not have quantity, please retype the command\n\n";
        }

        modelManager.getCurrentSession().getPrescriptions().clear();

        for (int i = 0; i < drugList.size(); i++) {
            modelManager.getCurrentSession().getPrescriptions().add(
                    new Prescription(drugList.get(i), Integer.valueOf(qtyList.get(i))));
        }

        return formatPrescription() + "Prescription added for patient: "
                + modelManager.getCurrentSession().getPatient().getName() + "\n\n";
    }

    public String endConsultation() {
        if (modelManager.getCurrentSession() == null) {
            throw new ConsultationException("No consultation session at the moment.");
        }

        if (modelManager.getCurrentSession().getDiagnosis().getAssessment() == null
                || modelManager.getCurrentSession().getDiagnosis().getSymptoms() == null ) {
            throw new ConsultationException("No diagnosis given for patient. Consultation incomplete.");
        }

        if (modelManager.getCurrentSession().getPrescriptions().isEmpty()) {
            throw new ConsultationException("No diagnosis given for patient. Consultation incomplete");
        }

        modelManager.getConsultationList().add(modelManager.getCurrentSession());
        String result = formatDiagnosis() + formatPrescription();
        modelManager.endConsultation();
        return result + "Consultation session ended \n\n";
    }

    // formatting methods

    private String formatDiagnosis() {
        StringBuilder sb = new StringBuilder();
        sb.append("Diagnosis for patient: " + modelManager.getCurrentSession().getPatient().getName() + "\n");
        sb.append("==============================\n");
        sb.append("Assessment: " + modelManager.getCurrentSession().getDiagnosis().getAssessment() + "\n");
        sb.append("------------------------------\n");
        sb.append("Symptoms:\n");
        for (String symptom: modelManager.getCurrentSession().getDiagnosis().getSymptoms()) {
            sb.append("- " + symptom + "\n");
        }
        sb.append("------------------------------\n");
        return sb.toString();
    }

    private String formatPrescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prescription: \n");
        sb.append("==============================\n");
        for (Prescription prescription: modelManager.getCurrentSession().getPrescriptions()) {
            sb.append("- " + prescription.getDrug() + " (" + prescription.getQuantity() + ")" + "\n");
        }
        sb.append("------------------------------\n");
        return sb.toString();
    }


    // parsing methods
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    public static String parseSymptom(String symptom) {
        return symptom.trim();
    }

    public static ArrayList<String> parseSymptoms(Collection<String> symptoms) {
        final ArrayList<String> symptomList = new ArrayList<>();
        for (String symptom : symptoms) {
            symptomList.add(parseSymptom(symptom));
        }
        return symptomList;
    }

}


