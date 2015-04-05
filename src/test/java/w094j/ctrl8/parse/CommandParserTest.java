//@author A0065517A
package w094j.ctrl8.parse;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import w094j.ctrl8.database.config.CommandConfig;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.statement.CommandType;

/**
 * Tests parsing of the basic variant of Commands
 */
@RunWith(value = Parameterized.class)
public class CommandParserTest {

    private static CommandParser parser;
    private CommandType expected;
    private String input;

    /**
     * Creates a Command Test with the input and expected Command enum.
     *
     * @param input
     *            string to be parsed.
     * @param expected
     *            Command enum to be expected, null will mean that
     *            ParseException is expected.
     */
    public CommandParserTest(String input, CommandType expected) {
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
                { "add", CommandType.ADD },
                { "alias", CommandType.ALIAS },
                { "alias-add", CommandType.ALIAS_ADD },
                { "alias-delete", CommandType.ALIAS_DELETE },
                { "delete", CommandType.DELETE },
                { "done", CommandType.DONE },
                { "exit", CommandType.EXIT },
                { "help", CommandType.HELP },
                { "history", CommandType.HISTORY },
                { "history-clear", CommandType.HISTORY_CLEAR },
                { "history-undo", CommandType.HISTORY_UNDO },
                { "modify", CommandType.MODIFY },
                { "search", CommandType.SEARCH },
                { "view", CommandType.VIEW },

                // @author A0110787A
                /**
                 * Errornous tests
                 */
                { null, null },
                { "", null },
                { "some really long text", null }, // multiple words
                { "add-alias", null }, // lazy pattern detection
                { ".add", null }, // not 100% match
                { "clearhistory", null }, // detect whether symbols are caught
                { "saerch", null }, // typo
                /**
                 * Extreme tests
                 */
                { "add               ", CommandType.ADD }, // buffers

        });
    }

    /**
     * Initializes the command parser.
     */
    @BeforeClass
    public static void initParser() {
        CommandConfig config = new CommandConfig();
        parser = new CommandParser(config);
    }

    // @author A0065517A
    // @formatter:on

    /**
     * Tests the parsing of the command text
     */
    @Test
    public void test() {
        try {
            assertEquals(this.expected, parser.parse(this.input));
        } catch (ParseException pe) {
            assertEquals(null, this.expected);
        }
    }

}
