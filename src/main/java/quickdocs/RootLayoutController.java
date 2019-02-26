package quickdocs;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import quickdocs.logic.QuickDocsParser;
import quickdocs.model.QuickDocsModelManager;

/**
 * This class handles user interaction with the root layout
 */
public class RootLayoutController {

    private static int currentInputPointer = 0;

    @FXML
    private TextArea display;

    @FXML
    private TextField userInput;

    @FXML
    private TextArea inputFeedback;

    private QuickDocsParser parser;
    private QuickDocsModelManager modelManager;

    public RootLayoutController() {
        modelManager = new QuickDocsModelManager();
        parser = new QuickDocsParser(modelManager);
    }

    /**
     * This method will pass the command into the parser whenever the user presses enter
     * @param event Event associated with the user pressing enter to confirm a command
     */
    @FXML
    public void enterInput(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                inputFeedback.setText("");
                String result = parser.parseCommand(userInput.getText());
                display.appendText(result);
                display.appendText("\n");
                userInput.setText("");
                display.selectPositionCaret(display.getLength());
            } catch (Exception e) {
                inputFeedback.setText(e.toString());
            }
        }
    }

    /**
     * This method will check if the parameters entered into the command text field is valid
     * by calling the various checkers across the module
     * @param event Event associated with the user pressing space bar between parameters
     */
    @FXML
    public void checkInput(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            inputFeedback.setText("space entered");
        }
    }

    /**
     * This method allow other modules to tap on the display to show the output
     * of different commands
     * @return the reference to the display textArea for other modules
     */
    public TextArea getDisplay() {
        return this.display;
    }
}
