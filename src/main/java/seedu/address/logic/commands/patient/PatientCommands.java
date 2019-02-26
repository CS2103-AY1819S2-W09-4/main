package seedu.address.logic.commands.patient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Stream;

import quickdocs.model.QuickDocsModelManager;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.exception.PatientException;
import seedu.address.model.tag.Tag;

/**
 * Consist of all the commands pertaining to patient records
 */
public class PatientCommands {

    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_NRIC = new Prefix("r/");
    public static final Prefix PREFIX_DOB = new Prefix("d/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_CONTACT = new Prefix("c/");
    public static final Prefix PREFIX_GENDER = new Prefix("g/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");

    private QuickDocsModelManager modelManager;

    public void setModelManager(QuickDocsModelManager modelManager) {
        this.modelManager = modelManager;
    }

    /**
     * Add a single patient record into QuickDocs
     * @param args arguments that corresponds to the patient class' attributes
     * @return a string to display the newly created patient record's detail
     * @throws PatientException if insufficient arguments are supplied
     */
    public String addPatient(String args) throws PatientException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_NRIC, PREFIX_DOB, PREFIX_ADDRESS, PREFIX_EMAIL,
                        PREFIX_CONTACT, PREFIX_GENDER, PREFIX_TAG);

        boolean prefixesPresent = arePrefixesPresent(argMultimap, PREFIX_NAME,
                PREFIX_NRIC, PREFIX_DOB, PREFIX_ADDRESS, PREFIX_EMAIL,
                PREFIX_CONTACT, PREFIX_GENDER);
        boolean preamblePresent = argMultimap.getPreamble().isEmpty();

        if (!prefixesPresent || !preamblePresent) {
            throw new PatientException("Some details are left out, please retype the command");
        }

        String name = argMultimap.getValue(PREFIX_NAME).get().trim();
        String nric = argMultimap.getValue(PREFIX_NRIC).get().trim();
        Calendar dob = parseDob(argMultimap.getValue(PREFIX_DOB).get().trim());
        String address = argMultimap.getValue(PREFIX_ADDRESS).get().trim();
        String email = argMultimap.getValue(PREFIX_EMAIL).get().trim();
        int contact = Integer.valueOf(argMultimap.getValue(PREFIX_CONTACT).get().trim());
        char gender = argMultimap.getValue(PREFIX_GENDER).get().trim().charAt(0);
        ArrayList<Tag> tagList = parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Patient patient = new Patient(name, nric, dob, address, email, contact, gender, tagList);

        modelManager.getPatientList().add(patient);

        StringBuilder sb = new StringBuilder();
        sb.append("Patient created: \n");
        sb.append("==============================\n");
        sb.append(patient.toString());
        sb.append("==============================\n");

        return sb.toString();
    }

    /**
     * Edits a selected patient via index, by supplying in attributes to replace existing
     * ones
     * @param args specific attributes to replace existing patient data
     * @return an acknowledgement message and displays the patient data to reflect changes made
     * @throws PatientException if insufficient arguments are entered
     */
    public String editPatient(String args) throws PatientException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_NRIC, PREFIX_DOB, PREFIX_ADDRESS, PREFIX_EMAIL,
                        PREFIX_CONTACT, PREFIX_GENDER, PREFIX_TAG);

        if (argMultimap.getPreamble().isEmpty()) {
            throw new PatientException("Some details are left out, please retype the command");
        }

        int index = Integer.valueOf(argMultimap.getPreamble());
        Patient currentPatient = modelManager.getPatientList().get(index);
        Patient editedPatient = createEditedPatient(argMultimap, currentPatient);

        modelManager.getPatientList().set(index, editedPatient);

        StringBuilder sb = new StringBuilder();
        sb.append("Patient edited: \n");
        sb.append("==============================\n");
        sb.append(modelManager.getPatientList().get(index).toString());
        sb.append("==============================\n");

        return sb.toString();
    }


    /**
     * List a group of patients having the same tag, similar NRIC or name.
     * If index is given, show specific patient at that index
     * @param args name, nric or tags of the group of patients to be listed, or index of specific patient
     * @return patient details in string format to display on ui
     * @throws PatientException if there are no patient records, or invalid arguments are entered
     */
    public String listPatient(String args) throws PatientException {

        if (modelManager.getPatientList().size() < 1) {
            throw new PatientException("No patient records");
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_NRIC, PREFIX_TAG);

        if (!argMultimap.getPreamble().isEmpty()) {

            if (!argMultimap.getPreamble().trim().matches("\\d+")) {
                throw new PatientException("Index should be numeric");
            }

            int index = Integer.valueOf(argMultimap.getPreamble());
            if (index >= modelManager.getPatientList().size() || index < 0) {
                throw new PatientException("Invalid index");
            }

            Patient selectedPatient = modelManager.getPatientList().get(index);
            return formatPatientDetails(selectedPatient);
        }

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            // list patient by name, can get multiple records
            String name = argMultimap.getValue(PREFIX_NAME).get();
            return findPatientsByNameOrNric(name, PREFIX_NAME.getPrefix());
        }

        if (argMultimap.getValue(PREFIX_NRIC).isPresent()) {
            // list patient by nric
            String nric = argMultimap.getValue(PREFIX_NRIC).get();
            return findPatientsByNameOrNric(nric, PREFIX_NRIC.getPrefix());
        }

        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            // list all patients with the same tag
            return findPatientsByTag(new Tag(argMultimap.getValue(PREFIX_TAG).get()));
        }

        // try to list all patients
        StringBuilder allPatients = new StringBuilder();
        allPatients.append("Listing patients:\n");
        allPatients.append("==============================\n");
        for (int i = 0; i < modelManager.getPatientList().size(); i++) {
            if (i == 50) {
                allPatients.append("Too many records, try and narrow down search with names, nric or tags\n");
                break;
            }
            Patient patient = modelManager.getPatientList().get(i);

            Calendar dob = patient.getDob();
            int year = dob.get(Calendar.YEAR);
            int month = dob.get(Calendar.MONTH);
            int day = dob.get(Calendar.DAY_OF_MONTH);

            allPatients.append(i + ") " + patient.getName()
                    + " " + patient.getNric()
                    + " " + patient.getGender()
                    + " " + day + "-" + month + "-" + year
                    + "\n"
            );
        }
        allPatients.append("\n");
        return allPatients.toString();
    }

    // editing methods

    /**
     * This method will check the argument multimap and see if there are changes to be made, and returns
     * the patient object with the changes applied
     * @param argMultimap consist of all the changes to be made to the current patient record
     * @param originalPatient the current patient record to clone and edit
     * @return current patient object with the changes made
     */
    public static Patient createEditedPatient(ArgumentMultimap argMultimap, Patient originalPatient) {
        Patient editedPatient = new Patient(originalPatient);

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editedPatient.setName(argMultimap.getValue(PREFIX_NAME).get());
        }

        if (argMultimap.getValue(PREFIX_NRIC).isPresent()) {
            editedPatient.setNric(argMultimap.getValue(PREFIX_NRIC).get());
        }

        if (argMultimap.getValue(PREFIX_DOB).isPresent()) {
            editedPatient.setDob(parseDob(argMultimap.getValue(PREFIX_DOB).get()));
        }

        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editedPatient.setAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        }

        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editedPatient.setEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        }

        if (argMultimap.getValue(PREFIX_CONTACT).isPresent()) {
            editedPatient.setContact(Integer.valueOf(argMultimap.getValue(PREFIX_CONTACT).get()));
        }

        if (argMultimap.getValue(PREFIX_GENDER).isPresent()) {
            editedPatient.setGender(argMultimap.getValue(PREFIX_NAME).get().charAt(0));
        }

        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            ArrayList<Tag> tagList = parseTags(argMultimap.getAllValues(PREFIX_TAG));
            editedPatient.setTags(tagList);
        }

        return editedPatient;
    }

    // listing methods

    /**
     * Find patients with similar name or nric numbers
     * @param searchSequence the common name or nric sequence to group patients up
     * @param prefix to indicate whether to group by nric or name
     * @return a single patient's details or a list of patients and their indexes fulfilling the search
     * criteria
     */
    public String findPatientsByNameOrNric(String searchSequence, String prefix) {
        ArrayList<Patient>foundPatients = new ArrayList<>();
        ArrayList<Integer>foundPatientsIndexes = new ArrayList<>();

        for (int i = 0; i < modelManager.getPatientList().size(); i++) {
            Patient patient = modelManager.getPatientList().get(i);
            if (prefix.equals(PREFIX_NRIC.getPrefix())
                    && patient.getNric().toLowerCase()
                    .matches("^" + searchSequence.toLowerCase() + ".*")) {
                foundPatients.add(patient);
                foundPatientsIndexes.add(i);
            }
            if (prefix.equals(PREFIX_NAME.getPrefix())
                    && patient.getName().toLowerCase()
                    .matches("^" + searchSequence.toLowerCase() + ".*")) {
                foundPatients.add(patient);
                foundPatientsIndexes.add(i);
            }
        }

        if (foundPatients.size() == 0) {
            return "No patient record found\n\n";
        }

        if (foundPatients.size() > 1) {
            return formatMultiplePatients(foundPatients, foundPatientsIndexes);
        }

        return formatPatientDetails(foundPatients.get(0));
    }

    /**
     * find patients with same tag that the user entered
     */
    public String findPatientsByTag(Tag searchSequence) {
        ArrayList<Patient>foundPatients = new ArrayList<>();
        ArrayList<Integer>foundPatientsIndexes = new ArrayList<>();

        for (int i = 0; i < modelManager.getPatientList().size(); i++) {
            Patient currentPatient = modelManager.getPatientList().get(i);
            if (currentPatient.getTags().contains(searchSequence)) {
                foundPatients.add(currentPatient);
                foundPatientsIndexes.add(i);
            }
        }

        if (foundPatients.size() == 0) {
            return "No patient record found\n\n";
        }

        if (foundPatients.size() > 1) {
            return formatMultiplePatients(foundPatients, foundPatientsIndexes);
        }

        return formatPatientDetails(foundPatients.get(0));
    }

    /**
     * format the patient details into a single string for displaying on the ui
     */
    public static String formatPatientDetails(Patient patient) {
        Calendar dob = patient.getDob();
        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        StringBuilder sb = new StringBuilder();
        sb.append("Printing patient details\n");
        sb.append("=============================\n");
        sb.append("name: " + patient.getName() + "\n");
        sb.append("nric: " + patient.getNric() + "\n");
        sb.append("date of birth: " + day + "-" + month + "-" + year + "\n");
        sb.append("address: " + patient.getAddress() + "\n");
        sb.append("email: " + patient.getEmail() + "\n");
        sb.append("contact: " + patient.getContact() + "\n");
        sb.append("gender: " + patient.getGender() + "\n");
        sb.append(formatTags(patient.getTags()) + "\n");

        return sb.toString();
    }

    /**
     * format a list of patients into a single string for displaying on the ui
     */
    public static String formatMultiplePatients(ArrayList<Patient> patients, ArrayList<Integer> patientIndexes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Listing patients:\n");
        sb.append("==============================\n");
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);

            Calendar dob = patient.getDob();
            int year = dob.get(Calendar.YEAR);
            int month = dob.get(Calendar.MONTH);
            int day = dob.get(Calendar.DAY_OF_MONTH);

            sb.append(patientIndexes.get(i) + ") " + patient.getName()
                    + " " + patient.getNric()
                    + " " + patient.getGender()
                    + " " + day + "-" + month + "-" + year
                    + "\n"
            );
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Format all the tags a single patient have into a single string to display on ui
     */
    public static String formatTags(List<Tag> tagList) {

        StringBuilder listOfTags = new StringBuilder();

        for (Tag tag : tagList) {
            listOfTags.append(tag.tagName + " ");
        }
        return listOfTags.toString();
    }

    // parsing methods
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     *  returns a tag created from the command argument
     */
    public static Tag parseTag(String tag) {
        String trimmedTag = tag.trim();
        return new Tag(trimmedTag);
    }

    /**
     * parse the list of tags entered into a list of tags to create the patient record with
     */
    public static ArrayList<Tag> parseTags(Collection<String> tags) {
        final ArrayList<Tag> tagList = new ArrayList<>();
        for (String tagName : tags) {
            tagList.add(parseTag(tagName));
        }
        return tagList;
    }

    /**
     * parse the date of birth entered by the user during the creation of the patient record
     */
    public static Calendar parseDob(String dob) {
        String[] splitDob = dob.split("-");
        Calendar calendar = new GregorianCalendar(Integer.valueOf(splitDob[2]),
                Integer.valueOf(splitDob[1]), Integer.valueOf(splitDob[0]));
        return calendar;
    }
}
