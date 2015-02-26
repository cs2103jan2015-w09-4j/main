//@author A0110787A
package w094j.ctrl8.terminal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import w094j.ctrl8.display.CLIDisplay;
import w094j.ctrl8.message.NormalMessage;

public class TerminalBasicHelpTest {
    private String expected;
    private String output;

    public TerminalBasicHelpTest() {
        this.expected = NormalMessage.HELP_ALL;
    }

    @Test
    public void basicHelpTest() {
        // Setup terminal and a CLIDisplay to go with it
        CLIDisplay d = new CLIDisplay();
        Terminal testTerminal = new Terminal(d);

        // Function to test
        testTerminal.help();

        // Output from terminal
        this.output = d.getLastMessage();

        assertEquals(this.expected, this.output);
    }

}
