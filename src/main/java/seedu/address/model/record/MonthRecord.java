package seedu.address.model.record;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the records for a month, and the statistics relevant to it.
 */
public class MonthRecord {
    private Statistics stats;
    private YearMonth yearMonth;
    private int recordPointer;
    private ArrayList<Record> records;

    public MonthRecord(YearMonth yearMonth) {
        this.stats = new Statistics();
        this.yearMonth = yearMonth;
        this.recordPointer = 0;
        this.records = new ArrayList<>();
    }

    public Statistics getStatistics() {
        return this.stats;
    }

    /**
     * Updates the statistics of the current month to match the records in records.
     */
    private void updateStatistics() {
        if (recordPointer < records.size()) {
            Statistics subStats = Statistics.fromRecordList(this.records.subList(recordPointer, records.size()));
            this.stats = this.stats.merge(subStats);
        }
        recordPointer = records.size();
    }

    /**
     * Adds a record to the MonthRecord, and updates the statistics.
     * @param record A single Record object
     */
    public void addRecord(Record record) {
        this.records.add(record);
        this.updateStatistics();
    }

    /**
     * Adds a list of records to the MonthRecord, and updates the statistics.
     * @param records A list of Record objects
     */
    public void addRecord(List<Record> records) {
        this.records.addAll(records);
        this.updateStatistics();
    }
}
