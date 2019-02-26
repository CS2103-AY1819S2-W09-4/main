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

    public void setModelManager(QuickDocsModelManager modelManager) {
        this.modelManager = modelManager;
    }


    /**
     * Add diagnosis (symptoms and assessment) for current consultation session
     * @param args symptoms prefixed with s/ and one assessment prefixed with a/
     * @return indication of diagnosis added to be displayed
     */
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

        return formatDiagnosis(consultation) + "Diagnosis recorded for patient: "
                + currentPatient.getName() + "\n\n";
    }

    /**
     * Replace the diagnosis given in the current consultation with the user's input
     * @param args the symptoms and assessment of the diagnosis to replace the current diagnosis
     * @return the details of the edited diagnosis to be displayed on the UI
     */
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

        return formatDiagnosis(modelManager.getCurrentSession()) + "Diagnosis recorded for patient: "
                + consultation.getPatient().getName() + "\n\n";

    }

    /**
     * Add the prescriptions to the current consultation session
     * @param args the drugs and the quantity prescribed to tackle the symptoms
     * @return the list of drugs and quantity issued to the patient
     */
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

        return formatPrescription(modelManager.getCurrentSession()) + "Prescription added for patient: "
                + modelManager.getCurrentSession().getPatient().getName() + "\n\n";
    }

    /**
     * End the current consultation session, no further changes are to be made
     * @return the details of both the diagnosis and the prescription of the consultation session.
     */
    public String endConsultation() {
        if (modelManager.getCurrentSession() == null) {
            throw new ConsultationException("No consultation session at the moment.");
        }

        if (modelManager.getCurrentSession().getDiagnosis().getAssessment() == null
                || modelManager.getCurrentSession().getDiagnosis().getSymptoms() == null) {
            throw new ConsultationException("No diagnosis given for patient. Consultation incomplete.");
        }

        if (modelManager.getCurrentSession().getPrescriptions().isEmpty()) {
            throw new ConsultationException("No diagnosis given for patient. Consultation incomplete");
        }

        modelManager.getCurrentSession().setIndex(modelManager.getConsultationList().size());
        modelManager.getConsultationList().add(modelManager.getCurrentSession());
        String result = formatDiagnosis(modelManager.getCurrentSession())
                + formatPrescription(modelManager.getCurrentSession());
        modelManager.endConsultation();
        return result + "Consultation session ended \n\n";
    }

    /**
     * List the consultation sessions a single patient had previously, if index is supplied
     * show the details of that particular consultation
     * @param args index or NRIC of the patient
     * @return either the diagnosis and prescription of a single consultation (index) or a list
     * of consultations with the index for subsequent searches
     */
    public String listConsultation(String args) {
        if (modelManager.getConsultationList().size() < 1) {
            throw new ConsultationException("There are currently no consultation records");
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NRIC);

        if (argMultimap.getPreamble().isEmpty() && !argMultimap.getValue(PREFIX_NRIC).isPresent()) {
            throw new ConsultationException("Please provide an index or nric");
        }

        //if only index given
        if (!argMultimap.getPreamble().isEmpty()) {

            if (!argMultimap.getPreamble().trim().matches("\\d+")) {
                throw new ConsultationException("Index should be numeric");
            }

            int index = Integer.valueOf(argMultimap.getPreamble());
            if (index >= modelManager.getConsultationList().size() || index < 0) {
                throw new ConsultationException("Invalid index");
            }

            Consultation consultation = modelManager.getConsultationList().get(index);

            StringBuilder sb = new StringBuilder();
            sb.append("Displaying consultation:\n");
            sb.append("==============================\n");
            sb.append(formatDiagnosis(consultation));
            sb.append(formatPrescription(consultation));
            sb.append("------------------------------\n");

            return sb.toString();
        }

        ArrayList consultations = modelManager.getConsultationByNric(argMultimap.getValue(PREFIX_NRIC).get());

        if (consultations.size() == 0) {
            throw new ConsultationException("No consultation belonging to patient indicated.");
        }

        return formatConsultations(consultations);

    }

    // formatting methods

    /**
     * formats the diagnosis details for displaying on ui
     */
    private String formatDiagnosis(Consultation consultation) {
        StringBuilder sb = new StringBuilder();
        sb.append("Diagnosis for patient: " + consultation.getPatient().getName() + "\n");
        sb.append("==============================\n");
        sb.append("Assessment: " + consultation.getDiagnosis().getAssessment() + "\n");
        sb.append("------------------------------\n");
        sb.append("Symptoms:\n");
        for (String symptom: consultation.getDiagnosis().getSymptoms()) {
            sb.append("- " + symptom + "\n");
        }
        sb.append("------------------------------\n");
        return sb.toString();
    }

    /**
     * formats the prescription details for displaying on ui
     */
    private String formatPrescription(Consultation consultation) {
        StringBuilder sb = new StringBuilder();
        sb.append("Prescription: \n");
        sb.append("==============================\n");
        for (Prescription prescription: consultation.getPrescriptions()) {
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

    /**
     * consolidate the list of symptoms entered by user into a single arraylist
     */
    public static ArrayList<String> parseSymptoms(Collection<String> symptoms) {
        final ArrayList<String> symptomList = new ArrayList<>();
        for (String symptom : symptoms) {
            symptomList.add(parseSymptom(symptom));
        }
        return symptomList;
    }

    /**
     * format the list of consultation for display whenever a NRIC is supplied to search
     * for consultation sessions
     */
    public String formatConsultations(ArrayList<Consultation> consultations) {
        StringBuilder sb = new StringBuilder();
        sb.append("Listing consultations:\n");
        sb.append("==============================\n");
        for (int i = 0; i < consultations.size(); i++) {
            Consultation consultation = consultations.get(i);
            sb.append(consultation.getIndex() + ") ");
            sb.append(consultation.getDiagnosis().getAssessment() + "\n");
        }
        sb.append("------------------------------\n");
        return sb.toString();
    }

}


