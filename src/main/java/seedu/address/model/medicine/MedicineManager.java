package seedu.address.model.medicine;

import static seedu.address.commons.util.CollectionUtil.binarySearch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

/**
 * An model for overall storage of medicine
 */
public class MedicineManager {

    private Directory root;
    private ArrayList<Medicine> listOfMedicine;

    public MedicineManager() {
        root = new Directory("root");
        listOfMedicine = new ArrayList<>();
    }

    public void addMedicine(String medicineName, String[] path) {
        this.addMedicine(medicineName, 0, path);
    }

    /**
     * To add a medicine to the storage by specifying the name of medicine, quantity of it and the path
     * @param medicineName name of medicine
     * @param quantity quantity of medicine
     * @param path the path to store to
     */
    public void addMedicine(String medicineName, int quantity, String[] path) {
        Optional<Directory> directory = root.findDirectory(path, 0);
        if (!directory.isPresent()) {
            throw new IllegalArgumentException("Invalid Path");
        }
        Medicine medicine = new Medicine(medicineName, quantity);
        listOfMedicine.add(medicine);
        listOfMedicine.sort(Comparator.comparing((Medicine x) -> (x.name)));
        directory.get().addMedicine(medicine);
    }

    /**
     * Add a new directory under a directory specified by the path
     * @param directoryName the name of the new directory
     * @param path the path of the destination directory
     */
    public void addDirectory(String directoryName, String[] path) {
        Optional<Directory> directory = root.findDirectory(path, 0);
        if (!directory.isPresent()) {
            throw new IllegalArgumentException("Invalid path");
        }
        directory.get().addDirectory(directoryName);
    }

    /**
     * find a medicine by its name
     * @param medicineName the medicine name to search for
     * @return Optional.empty() if there is no medicine with the desired name;
     *         Optional.of(E) if E's name matches the key
     */
    public Optional<Medicine> findMedicine(String medicineName) {
        Comparator comparator = Comparator.naturalOrder();
        return binarySearch(
                listOfMedicine, (Medicine med)->(comparator.compare(med.name, medicineName)));
    }

    public Optional<Medicine> findMedicine(String[] path) {
        return root.findMedicine(path, 0);
    }

    /**
     * record a purchase of medicine in the medicine storage
     * @param path the path leading to the medicine
     * @param quantity quantity of medicine purchased
     */
    public void purchaseMedicine(String[] path, int quantity) {
        Optional<Medicine> medicine = findMedicine(path);
        if (!medicine.isPresent()) {
            throw new IllegalArgumentException("No medicine found at the given path");
        }
        medicine.get().addQuantity(quantity);
    }

    /**
     * record a purchase of medicine in the medicine storage
     * @param medicineName the name of the medicine
     * @param quantity quantity of medicine purchased
     */
    public void purchaseMedicine(String medicineName, int quantity) {
        Optional<Medicine> medicine = findMedicine(medicineName);
        if (!medicine.isPresent()) {
            throw new IllegalArgumentException("No medicine with the given name");
        }
        medicine.get().addQuantity(quantity);
    }
}