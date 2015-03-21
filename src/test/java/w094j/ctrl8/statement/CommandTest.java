//@author A0065517A
package w094j.ctrl8.statement;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests parsing of the basic variant of Commands
 */
@RunWith(value = Parameterized.class)
public class CommandTest {

    private Command expected;
    private String input;

    /**
     * Creates a Command Test with the input and expected Command enum.
     *
     * @param input
     *            string to be parsed.
     * @param expected
     *            Command enum to be expected.
     */
    public CommandTest(String input, Command expected) {
        this.input = input;
        this.expected = expected;
    }

    /**
     * @return test data.
     */
    @Parameters(name = "{index}: Parse \"{0}\" to enum({1})")
    // @formatter:off
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /**
                 * Normal tests
                 */
                // Includes all the supported commands as of v0.2
                { "add", Command.ADD }, { "alias", Command.ALIAS },
                { "alias-add", Command.ALIAS_ADD },
                { "alias-delete", Command.ALIAS_DELETE },
                { "delete", Command.DELETE }, { "done", Command.DONE },
                { "exit", Command.EXIT }, { "help", Command.HELP },
                { "history", Command.HISTORY },
                { "history-clear", Command.HISTORY_CLEAR },
                { "history-undo", Command.HISTORY_UNDO },
                { "modify", Command.MODIFY }, { "search", Command.SEARCH },
                { "view", Command.VIEW },

                // @author A0110787A
                /**
                 * Errornous tests
                 */
                { null, null },
                { "some really long text", null }, // multiple words
                { "\r\n", null }, // some unique regex
                { "add-alias", null }, // lazy pattern detection
                { ".add", null }, // not 100% match
                { "clearhistory", null }, // detect whether symbols are caught
                { "saerch", null }, // typo
                /**
                 * Extreme tests
                 */
                { "HiStOrY-ClEaR", Command.HISTORY_CLEAR }, // cap and non-caps
                { "           add               ", Command.ADD }, // buffers
                { "\r\n    `!@#$%^&*()_+{}[]:'<>/.,~/*-         exit    ",
                        Command.EXIT } // various useless symbols

                });
    }

    // @author A0065517A
    // @formatter:on

    /**
     * Tests the parsing of the command text
     */
    @Test
    public void test() {
        assertEquals(this.expected, Command.parse(this.input));
    }

}
