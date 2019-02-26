package seedu.address.logic.commands.patient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import quickdocs.model.QuickDocsModelManager;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.patient.Prescription;

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


    // formatting methods

    private String formatDiagnosis(){
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

    private String formatPrescription(){
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


