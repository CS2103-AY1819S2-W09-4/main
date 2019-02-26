package seedu.address.logic.Patient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import quickdocs.model.QuickDocsModelManager;
import seedu.address.logic.commands.patient.PatientCommands;
import seedu.address.model.patient.exception.PatientException;
import seedu.address.testutil.Assert;

public class PatientCommandTest {

    private PatientCommands pc;
    private QuickDocsModelManager qdmm;

    @Before
    public void setUp() throws Exception {
        pc = new PatientCommands();
        qdmm = new QuickDocsModelManager();
        pc.setModelManager(qdmm);
    }

    @Test
    public void testAdd() {
        String args = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A"
                + " g/M d/01-02-1993 t/diabetes ";
        pc.addPatient(args);
        assertTrue(qdmm.getPatientList().size() == 1);
    }

    @Test
    public void invalidAdd1() {
        String args = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com r/S9123456A d/01-02-1993 ";
        Assert.assertThrows(PatientException.class, ()->pc.addPatient(args));
    }

    @Test
    public void invalidAdd2() {
        String args = " n/Peter Tan ";
        Assert.assertThrows(PatientException.class, ()->pc.addPatient(args));
    }

    @Test
    public void testEdit() {

        String args = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A"
                + " g/M d/01-02-1993 t/diabetes ";
        pc.addPatient(args);

        args = " 0 n/Peter Toh c/9111222 e/pt@hotmail.com";
        pc.editPatient(args);

        assertEquals(qdmm.getPatientList().get(0).getName(), "Peter Toh");
        assertEquals(qdmm.getPatientList().get(0).getContact(), 9111222);
        assertEquals(qdmm.getPatientList().get(0).getEmail(), "pt@hotmail.com");
    }

    @Test
    public void invalidEdit1() {
        // no patients
        String args = " 0 n/Peter Toh c/9111222 e/pt@hotmail.com";
        Assert.assertThrows(PatientException.class, ()->pc.editPatient(args));
    }

    @Test
    public void invalidEdit2() {
        // wrong index
        String args = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A"
                + " g/M d/01-02-1993 t/diabetes ";
        pc.addPatient(args);

        String args2 = " 2 n/Peter Toh c/9111222 e/pt@hotmail.com";
        Assert.assertThrows(PatientException.class, ()->pc.editPatient(args2));

        String args3 = " a n/Bjorn Toh";
        Assert.assertThrows(PatientException.class, ()->pc.editPatient(args3));
    }

    @Test
    public void invalidEdit3() {
        // no changes added
        String args = "";
        Assert.assertThrows(PatientException.class, ()->pc.editPatient(args));
    }

    @Test
    public void listPatientsByName() {
        String args1 = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A g/M "
                + "d/01-02-1993 t/diabetes ";
        String args2 = " n/Piper Wright a/3 diamond city e/pwright@gmail.com c/93333333 r/S9234568C"
                + " g/F d/03-03-1995 t/highbloodpressure";
        String args3 = " n/Perry Ng a/2 Shenton Road e/ssh@gmail.com c/92222222 r/S9234567B g/M "
                + " d/02-02-1995 t/highbloodpressure t/diabetes";
        pc.addPatient(args1);
        pc.addPatient(args2);
        pc.addPatient(args3);

        String result = pc.listPatient(" n/p");
        StringBuilder expected = new StringBuilder();
        expected.append("Listing patients:\n");
        expected.append("==============================\n");
        expected.append(0 + ") " + "Peter Tan"
                + " " + "S9123456A"
                + " " + "M"
                + " " + "1" + "-" + "2" + "-" + "1993"
                + "\n");
        expected.append(1 + ") " + "Piper Wright"
                + " " + "S9234568C"
                + " " + "F"
                + " " + "3" + "-" + "3" + "-" + "1995"
                + "\n");
        expected.append(2 + ") " + "Perry Ng"
                + " " + "S9234567B"
                + " " + "M"
                + " " + "2" + "-" + "2" + "-" + "1995"
                + "\n");
        expected.append("\n");
        assertEquals(result, expected.toString());
    }

    @Test
    public void invalidName() {
        String args1 = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A g/M "
                + "d/01-02-1993 t/diabetes ";
        pc.addPatient(args1);
        String result = pc.listPatient(" n/r");
        assertEquals(result, "No patient record found\n\n");
    }

    @Test
    public void listPatientsByNric() {
        String args1 = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A g/M "
                + "d/01-02-1993 t/diabetes ";
        String args2 = " n/Piper Wright a/3 diamond city e/pwright@gmail.com c/93333333 r/S9234568C"
                + " g/F d/03-03-1995 t/highbloodpressure";
        String args3 = " n/Perry Ng a/2 Shenton Road e/ssh@gmail.com c/92222222 r/S9234567B g/M "
                + " d/02-02-1995 t/highbloodpressure t/diabetes";
        pc.addPatient(args1);
        pc.addPatient(args2);
        pc.addPatient(args3);

        String result = pc.listPatient(" r/S92");
        StringBuilder expected = new StringBuilder();
        expected.append("Listing patients:\n");
        expected.append("==============================\n");
        expected.append(1 + ") " + "Piper Wright"
                + " " + "S9234568C"
                + " " + "F"
                + " " + "3" + "-" + "3" + "-" + "1995"
                + "\n");
        expected.append(2 + ") " + "Perry Ng"
                + " " + "S9234567B"
                + " " + "M"
                + " " + "2" + "-" + "2" + "-" + "1995"
                + "\n");
        expected.append("\n");
        assertEquals(result, expected.toString());

    }

    @Test
    public void invalidNric() {
        String args1 = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A g/M "
                + "d/01-02-1993 t/diabetes ";
        pc.addPatient(args1);
        String result = pc.listPatient(" r/S82");
        assertEquals(result, "No patient record found\n\n");
    }

    @Test
    public void listPatientByIndex() {
        String args1 = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A g/M "
                + "d/01-02-1993 t/diabetes ";
        String args2 = " n/Piper Wright a/3 diamond city e/pwright@gmail.com c/93333333 r/S9234568C"
                + " g/F d/03-03-1995 t/highbloodpressure";
        String args3 = " n/Perry Ng a/2 Shenton Road e/ssh@gmail.com c/92222222 r/S9234567B g/M "
                + " d/02-02-1995 t/highbloodpressure t/diabetes";
        pc.addPatient(args1);
        pc.addPatient(args2);
        pc.addPatient(args3);

        String result = pc.listPatient(" 2");
        StringBuilder sb = new StringBuilder();
        sb.append("Printing patient details\n");
        sb.append("=============================\n");
        sb.append("name: " + "Perry Ng" + "\n");
        sb.append("nric: " + "S9234567B" + "\n");
        sb.append("date of birth: " + "2" + "-" + "2" + "-" + "1995" + "\n");
        sb.append("address: " + "2 Shenton Road" + "\n");
        sb.append("email: " + "ssh@gmail.com" + "\n");
        sb.append("contact: " + 92222222 + "\n");
        sb.append("gender: " + "M" + "\n");
        sb.append("highbloodpressure " + "diabetes " + "\n");
        assertEquals(result, sb.toString());
    }

    @Test
    public void invalidIndex1() {
        // no patients
        Assert.assertThrows(PatientException.class, ()->pc.listPatient(" 0"));
    }

    @Test
    public void invalidIndex2() {
        String args1 = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A g/M "
                + "d/01-02-1993 t/diabetes ";
        pc.addPatient(args1);
        Assert.assertThrows(PatientException.class, ()->pc.listPatient(" 2"));
    }

    @Test
    public void listPatientByTags() {
        String args1 = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A g/M "
                + "d/01-02-1993 t/diabetes ";
        String args2 = " n/Piper Wright a/3 diamond city e/pwright@gmail.com c/93333333 r/S9234568C"
                + " g/F d/03-03-1995 t/highbloodpressure";
        String args3 = " n/Perry Ng a/2 Shenton Road e/ssh@gmail.com c/92222222 r/S9234567B g/M "
                + " d/02-02-1995 t/highbloodpressure t/diabetes";
        pc.addPatient(args1);
        pc.addPatient(args2);
        pc.addPatient(args3);

        String result = pc.listPatient(" t/diabetes");
        StringBuilder expected = new StringBuilder();
        expected.append("Listing patients:\n");
        expected.append("==============================\n");
        expected.append(0 + ") " + "Peter Tan"
                + " " + "S9123456A"
                + " " + "M"
                + " " + "1" + "-" + "2" + "-" + "1993"
                + "\n");
        expected.append(2 + ") " + "Perry Ng"
                + " " + "S9234567B"
                + " " + "M"
                + " " + "2" + "-" + "2" + "-" + "1995"
                + "\n");
        expected.append("\n");
        assertEquals(result, expected.toString());

    }

    @Test
    public void invalidTags() {
        String args1 = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A g/M "
                + "d/01-02-1993 t/diabetes ";
        String args2 = " n/Piper Wright a/3 diamond city e/pwright@gmail.com c/93333333 r/S9234568C"
                + " g/F d/03-03-1995 t/highbloodpressure";
        String args3 = " n/Perry Ng a/2 Shenton Road e/ssh@gmail.com c/92222222 r/S9234567B g/M "
                + " d/02-02-1995 t/highbloodpressure t/diabetes";
        pc.addPatient(args1);
        pc.addPatient(args2);
        pc.addPatient(args3);

        String result = pc.listPatient(" t/gout");
        assertEquals(result, "No patient record found\n\n");
    }

    @Test
    public void listAllPatients() {
        String args1 = " n/Peter Tan a/1 Simei Road e/ptan@gmail.com c/91111111 r/S9123456A g/M "
                + "d/01-02-1993 t/diabetes ";
        String args2 = " n/Piper Wright a/3 diamond city e/pwright@gmail.com c/93333333 r/S9234568C"
                + " g/F d/03-03-1995 t/highbloodpressure";
        String args3 = " n/Perry Ng a/2 Shenton Road e/ssh@gmail.com c/92222222 r/S9234567B g/M "
                + " d/02-02-1995 t/highbloodpressure t/diabetes";
        pc.addPatient(args1);
        pc.addPatient(args2);
        pc.addPatient(args3);

        String result = pc.listPatient(" ");
        StringBuilder expected = new StringBuilder();
        expected.append("Listing patients:\n");
        expected.append("==============================\n");
        expected.append(0 + ") " + "Peter Tan"
                + " " + "S9123456A"
                + " " + "M"
                + " " + "1" + "-" + "2" + "-" + "1993"
                + "\n");
        expected.append(1 + ") " + "Piper Wright"
                + " " + "S9234568C"
                + " " + "F"
                + " " + "3" + "-" + "3" + "-" + "1995"
                + "\n");
        expected.append(2 + ") " + "Perry Ng"
                + " " + "S9234567B"
                + " " + "M"
                + " " + "2" + "-" + "2" + "-" + "1995"
                + "\n");
        expected.append("\n");
        assertEquals(result, expected.toString());
    }

}
