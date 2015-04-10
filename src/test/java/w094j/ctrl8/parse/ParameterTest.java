//@author A0065517A
package w094j.ctrl8.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import w094j.ctrl8.database.config.ParameterConfig;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.statement.parameter.Parameter;
import w094j.ctrl8.parse.statement.parameter.ParameterContainer;

/**
 * Abstract class for test classes to extends this to test Long Explicit, Short
 * Explicit and Implicit.
 */
public abstract class ParameterTest {

    private ParameterContainer expectedParamContainer;
    private String input;
    private ParameterParser parser;

    /**
     * Creates a Parameter test.
     *
     * @param input
     *            The String to be sent for parsing.
     * @param expected
     *            Expected Parameter list, if null the test will expect
     *            Exception.
     * @param isExplicitShortMode
     *            Explicit short mode for the parser.
     * @param isImplicitMode
     *            Implicit mode for the parser.
     * @throws ParseException
     *             when the expected parameter list has issues.
     */
    public ParameterTest(String input, List<Parameter> expected,
            boolean isExplicitShortMode, boolean isImplicitMode)
            throws ParseException {
        this.input = input;
        if (expected != null) {
            this.expectedParamContainer = new ParameterContainer(expected);
        } else {
            this.expectedParamContainer = null;
        }
        ParameterConfig config = new ParameterConfig();
        config.setExplicitShortMode(isExplicitShortMode);
        config.setImplicitMode(isImplicitMode);
        this.parser = new ParameterParser(config);
    }

    /**
     * Tests the expected parameter list against the parsed parameter list.
     */
    @Test
    public void test() {

        try {
            assertEquals(this.expectedParamContainer,
                    this.parser.parse(this.input));
        } catch (ParseException e) {
            if (this.expectedParamContainer != null) {
                assertTrue(false);
            } else {
                assertTrue(true);
            }
        }

    }
}
