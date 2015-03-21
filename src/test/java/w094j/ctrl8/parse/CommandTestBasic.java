//@author A0065517A
package w094j.ctrl8.parse;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import w094j.ctrl8.database.config.CommandConfig;
import w094j.ctrl8.statement.CommandType;

/**
 * Tests parsing of the basic variant of Commands
 */
@RunWith(value = Parameterized.class)
public class CommandTestBasic {

    private CommandType expected;
    private String input;
    private CommandParser parser;

    /**
     * Creates a Command Test with the input and expected Command enum.
     *
     * @param input
     *            string to be parsed.
     * @param expected
     *            Command enum to be expected.
     */
    public CommandTestBasic(String input, CommandType expected) {
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
                { "add", CommandType.ADD }, { "alias", CommandType.ALIAS },
                { "alias-add", CommandType.ALIAS_ADD },
                { "alias-delete", CommandType.ALIAS_DELETE },
                { "delete", CommandType.DELETE }, { "done", CommandType.DONE },
                { "exit", CommandType.EXIT }, { "help", CommandType.HELP },
                { "history", CommandType.HISTORY },
                { "history-clear", CommandType.HISTORY_CLEAR },
                { "history-undo", CommandType.HISTORY_UNDO },
                { "modify", CommandType.MODIFY }, { "search", CommandType.SEARCH },
                { "view", CommandType.VIEW }, { "help", CommandType.HELP } });
    }

    /**
     * Initializes the command parser.
     */
    @Before
    public void initParser() {
        CommandConfig config = new CommandConfig();
        this.parser = new CommandParser(config);
    }

    // @formatter:on

    /**
     * Tests the parsing of the command text
     */
    @Test
    public void test() {
        assertEquals(this.expected, this.parser.parse(this.input));
    }

}
