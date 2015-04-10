package w094j.ctrl8.parse;

import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.statement.parameter.CategoryParameter;
import w094j.ctrl8.parse.statement.parameter.DeadlineParameter;
import w094j.ctrl8.parse.statement.parameter.DescriptionParameter;
import w094j.ctrl8.parse.statement.parameter.Parameter;
import w094j.ctrl8.parse.statement.parameter.PriorityParameter;
import w094j.ctrl8.parse.statement.parameter.TitleParameter;

//@author A0065517A
/**
 * Tests parsing of the short explicit parameters to its String representation.
 * This will not test implicit parameters, but will also test the
 * inter-operability of long parameter parameters with the short parameters.
 */
@RunWith(value = Parameterized.class)
public class ExplicitShortParameterTest extends ParameterTest {

    /**
     * Creates a Short Explicit Parameter test.
     *
     * @param input
     *            The parameter(s) to be parsed.
     * @param expected
     *            Expected Parsed result, if null exception is expected.
     * @throws ParseException
     *             when the expected parameter list has issues.
     */
    public ExplicitShortParameterTest(String input, List<Parameter> expected)
            throws ParseException {
        super(input, expected, true, false);
    }

    /**
     * @return test data.
     */
    @Parameters(name = "{index}: Parse \"{0}\" to get all the parameters({1})")
    //@formatter:off
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /**
                 * Normal tests
                 */
                // Test a simple parameter
                { "#nus", Arrays.asList(new Parameter[] {new CategoryParameter("nus")}) },
                // Ensures that Explicit short will not pick up any string after it
                { "-today +1pm", Arrays.asList(new Parameter[] {new DeadlineParameter("today"), new DescriptionParameter("1pm")}) },
                // Test if Long Explicit parameters are still working
                { "+{Remember to talk about Crawler technologies.}", Arrays.asList(new Parameter[] {new DescriptionParameter("Remember to talk about Crawler technologies.")}) },
                // Test if Long Explicit parameters work with short explicit parameters
                { "+{Remember to talk about Crawler technologies.} %10", Arrays.asList(new Parameter[] {new PriorityParameter("10"), new DescriptionParameter("Remember to talk about Crawler technologies.")}) },
                /**
                 * Extreme tests
                 */
                // An escaped = should be allowed in the parameter payload, and subsequently undelimited.
                { "=a\\=a", Arrays.asList(new Parameter[] {new TitleParameter("a=a")}) },
                // A character that is obviously not an parameter symbol should not be un-escaped
                { "=a\\;a", Arrays.asList(new Parameter[] {new TitleParameter("a\\;a")}) },
                // A valid parameter symbol should not be picked up when it does not fit the pattern of a parameter
                { "=a=a", Arrays.asList(new Parameter[] {new TitleParameter("a=a")}) },
                /**
                 * Errornous tests
                 */
                // Ensures that a valid parameter symbol will not get picked up, if there are no parameters
                { " = ", null },
                // Ensures that a non-symbol is not picked up
                { "<>", null },

        });
    }
    // @formatter:on

}
