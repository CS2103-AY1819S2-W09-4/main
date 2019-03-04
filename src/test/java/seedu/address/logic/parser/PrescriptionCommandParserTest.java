package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.ArrayList;

import org.junit.Test;

import seedu.address.logic.commands.PrescriptionCommand;

public class PrescriptionCommandParserTest {

    private PrescriptionCommandParser parser = new PrescriptionCommandParser();

    @Test
    public void successfulPrescription() {
        String userInput = " m/antibiotics q/1";
        ArrayList<String> medList = new ArrayList<>();
        ArrayList<Integer> qtyList = new ArrayList<>();
        medList.add("antibiotics");
        qtyList.add(1);
        assertParseSuccess(parser, userInput, new PrescriptionCommand(medList, qtyList));
    }

    @Test
    public void invalidPrescriptions() {
        String userInput = "m/antibiotics";
        assertParseFailure(parser, userInput,
                "Some details are left out, please retype the command");

        userInput = " m/antibiotics m/cough syrup q/1";
        assertParseFailure(parser, userInput,
                "Some medicine do not have assigned quantity, please reenter command");

        userInput = " m/antibiotics q/2 q/1";
        assertParseFailure(parser, userInput,
                "Additional quantities specified, please retype the command");

    }

}
