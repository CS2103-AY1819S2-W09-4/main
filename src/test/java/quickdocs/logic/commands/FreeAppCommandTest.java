package quickdocs.logic.commands;

import static quickdocs.testutil.TypicalAppointments.APP_A;
import static quickdocs.testutil.TypicalAppointments.APP_E;
import static quickdocs.testutil.TypicalAppointments.getTypicalAppointmentsQuickDocs;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import quickdocs.logic.CommandHistory;
import quickdocs.model.Model;
import quickdocs.model.ModelManager;
import quickdocs.model.QuickDocs;
import quickdocs.model.UserPrefs;

public class FreeAppCommandTest {
    private QuickDocs quickDocs = getTypicalAppointmentsQuickDocs();
    private Model model = new ModelManager(quickDocs, new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void executeFreeApp() {
        LocalDate start = APP_A.getDate();
        LocalDate end = start.plusDays(30);
        CommandResult result = new FreeAppCommand(start, end).execute(model, commandHistory);
        String expected = String.format(FreeAppCommand.MESSAGE_SUCCESS, start, end)
                + model.freeApp(start, end) + "\n";

        Assert.assertEquals(result.getFeedbackToUser(), expected);
    }

    @Test
    public void executeFreeAppNoFree() {
        model.addApp(APP_E);
        LocalDate start = APP_E.getDate();
        CommandResult result = new FreeAppCommand(start, start).execute(model, commandHistory);
        String expected = String.format(FreeAppCommand.MESSAGE_NO_FREE_SLOTS, start, start)
                + model.freeApp(start, start) + "\n";

        Assert.assertEquals(result.getFeedbackToUser(), expected);
    }

    @Test
    public void equals() {
        LocalDate start = APP_A.getDate();
        LocalDate endA = start.plusDays(10);
        LocalDate endB = start.plusDays(20);
        FreeAppCommand freeAppA = new FreeAppCommand(start, endA);
        FreeAppCommand freeAppB = new FreeAppCommand(start, endB);

        // same object -> returns true
        Assert.assertEquals(freeAppA, freeAppA);

        // same values -> returns true
        FreeAppCommand freeAppCopy = new FreeAppCommand(start, endA);
        Assert.assertEquals(freeAppA, freeAppCopy);

        // different types -> returns false
        Assert.assertNotEquals(freeAppA, 1);

        // null -> returns false
        Assert.assertNotEquals(freeAppA, null);

        // different attributes -> returns false
        Assert.assertNotEquals(freeAppA, freeAppB);
    }
}
