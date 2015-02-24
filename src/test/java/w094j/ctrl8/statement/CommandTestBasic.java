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
public class CommandTestBasic {

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
    public CommandTestBasic(String input, Command expected) {
        this.input = input;
        this.expected = expected;
    }

    /**
     * @return test data.
     */
    @Parameters(name = "{index}: Parse \"{0}\" to enum({1})")
    //@formatter:off
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /**
                 * Normal tests
                 */
                { "add", Command.ADD }, { "alias", Command.ALIAS },
                { "alias-add", Command.ALIAS_ADD }, { "alias-delete", Command.ALIAS_DELETE },
                { "delete", Command.DELETE }, { "done", Command.DONE },
                { "exit", Command.EXIT }, {"help", Command.HELP}, { "history", Command.HISTORY },
                { "history-clear", Command.HISTORY_CLEAR }, { "history-undo", Command.HISTORY_UNDO },
                { "modify", Command.MODIFY }, { "search", Command.SEARCH },
                { "view", Command.VIEW } });
    }

    //@formatter:on

    /**
     * Tests the parsing of the command text
     */
    @Test
    public void test() {
        assertEquals(this.expected, Command.parse(this.input));
    }

}
