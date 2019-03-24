package seedu.address.model.record;

import java.math.BigDecimal;
import java.time.YearMonth;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.consultation.Prescription;
import seedu.address.model.medicine.Medicine;

class MonthRecordTest {
    private MonthRecord monthRecord;
    private Record consultationRecord;
    private Record medicinePurchaseRecord;

    @BeforeEach
    void setUp() {
        Medicine medicine = new Medicine("test");
        monthRecord = new MonthRecord(YearMonth.of(2019, 1));
        consultationRecord = new ConsultationRecord(new Prescription(medicine, 1));

        medicinePurchaseRecord = new MedicinePurchaseRecord(medicine, 1, BigDecimal.valueOf(10.00));
    }


    @Test

    void getStatistics() {
        monthRecord.addRecord(consultationRecord);

        monthRecord.addRecord(medicinePurchaseRecord);
        Statistics stats = new Statistics(1, BigDecimal.valueOf(30.00), BigDecimal.valueOf(10.00));
        Assert.assertEquals(monthRecord.getStatistics(), stats);
    }

    @Test
    void addRecord() {
        monthRecord.addRecord(consultationRecord);
        monthRecord.addRecord(medicinePurchaseRecord);
        Assert.assertEquals(monthRecord.getNoOfRecords(), 2);
    }
}