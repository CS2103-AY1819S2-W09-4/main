package seedu.address.model.consultation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class AssessmentTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Assessment(null));
    }

    @Test
    public void invalidSymptoms() {
        Assert.assertThrows(IllegalArgumentException.class, ()->new Assessment(""));
        Assert.assertThrows(IllegalArgumentException.class, ()->new Assessment(" "));
        Assert.assertThrows(IllegalArgumentException.class, ()->new Assessment(",&/*"));
        Assert.assertThrows(IllegalArgumentException.class, ()->new Assessment("Coughing&"));
    }

    @Test
    public void equals() {
        Assessment assessment1 = new Assessment("Influenza");
        assertTrue(assessment1.equals(assessment1));

        Assessment assessment2 = new Assessment("Influenza");
        assertTrue(assessment1.equals(assessment2));

        Assessment assessment3 = new Assessment("Diarrhea");
        assertFalse(assessment1.equals(assessment3));
    }

}
