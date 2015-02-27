//@author A0110787A
package w094j.ctrl8.terminal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import w094j.ctrl8.display.CLIDisplay;
import w094j.ctrl8.message.CommandExecutionMessage;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.pojo.Task;

public class TerminalTest {
    private static String NO_EXCEPTION_THROWN = "No Exception Thrown";
    CLIDisplay display;
    Terminal testTerminal;
    private String expected;
    private String output;

    public TerminalTest() {
        // Setup terminal and a CLIDisplay to go with it
        this.display = new CLIDisplay();
        this.testTerminal = new Terminal(this.display);
    }

    @Test
    /*
     * [BASIC] Tests whether the correct exception is thrown when add() is given
     * a default-constructed task (which is incomplete by definition)
     */
    public void basicAddIncompleteTaskTest() {
        // Expected result
        this.expected = CommandExecutionMessage.EXCEPTION_IS_INCOMPLETE_TASK;

        // Function to test
        boolean caughtException = false;
        try {
            // Should work correctly regardless of Terminal's state
            this.testTerminal.add(new Task());
        } catch (Exception e) {
            assertEquals(this.expected, e.getMessage());
            caughtException = true;
        }
        if (!caughtException) {
            fail(NO_EXCEPTION_THROWN);
        }

    }

    @Test
    /*
     * [BASIC] Tests whether the correct exception is thrown when add() is given
     * a null object
     */
    public void basicAddNullTaskTest() {
        // Expected result
        this.expected = CommandExecutionMessage.EXCEPTION_NULL_TASK;

        // Function to test
        try {
            // Should work correctly regardless of Terminal's state
            this.testTerminal.add(null);

        } catch (Exception e) {
            assertEquals(this.expected, e.getMessage());
        }

    }

    @Test
    /*
     * [BASIC] Tests whether the help() function is working correctly with
     * CLIDisplay
     */
    public void basicHelpTest() {
        // Expected result
        this.expected = NormalMessage.HELP_ALL;

        // Function to test
        this.testTerminal.help();

        // Output from terminal
        this.output = this.display.getLastMessage();

        assertEquals(this.expected, this.output);
    }

}
