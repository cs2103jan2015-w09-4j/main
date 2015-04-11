//@author A0065517A
package w094j.ctrl8.parse;

import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.statement.parameter.DeadlineParameter;
import w094j.ctrl8.parse.statement.parameter.Parameter;
import w094j.ctrl8.parse.statement.parameter.StartTimeParameter;
import w094j.ctrl8.parse.statement.parameter.TitleParameter;

/**
 * Tests parsing of the implicit parameters to its String representation. The
 * tests will also test the inter-operability of long explicit parameters, short
 * explicit parameters and implicit parameters.
 */
@RunWith(value = Parameterized.class)
public class ImplicitParameterTest extends ParameterTest {

    /**
     * Creates a Explicit Parameter test.
     *
     * @param input
     *            The parameter(s) to be parsed.
     * @param expected
     *            Expected Parsed result, if null exception is expected.
     * @throws ParseException
     *             when the expected parameter list has issues.
     */
    public ImplicitParameterTest(String input, List<Parameter> expected)
            throws ParseException {
        super(input, expected, true, true);
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
                // Implicit Title for a task
                { " nus", Arrays.asList(new Parameter[] {new TitleParameter("nus")}) },
                // Implicit from and to for a task
                { " from 2pm to 4pm", Arrays.asList(new Parameter[] {new StartTimeParameter("2pm"), new DeadlineParameter("4pm")}) },
                // Implicit end time for a task
                { " due 4pm", Arrays.asList(new Parameter[] {new DeadlineParameter("4pm")}) },
                // A mix of Title, start and end time
                { " nus from 2pm to 4pm", Arrays.asList(new Parameter[] {new TitleParameter("nus"), new StartTimeParameter("2pm"), new DeadlineParameter("4pm")}) },
                { " \\#\\#\\#", Arrays.asList(new Parameter[] {new TitleParameter("###")}) },
                { " \\###", Arrays.asList(new Parameter[] {new TitleParameter("###")}) },
                { " \\##\\#", Arrays.asList(new Parameter[] {new TitleParameter("###")}) },
                { " warnings to taks that clash (timing-wising)", Arrays.asList(new Parameter[] {new TitleParameter("warnings to taks that clash (timing-wising)")}) },
                /**
                 * Extreme tests
                 */
                // Implicit Title containing a non-parameter symbol
                { " <>", Arrays.asList(new Parameter[] {new TitleParameter("<>")}) },
                // Implicit From and To that do not contain a valid date, should not parse as start and end date but as title
                { " Take Bus from UTown to Clementi", Arrays.asList(new Parameter[] {new TitleParameter("Take Bus from UTown to Clementi")}) },
                /**
                 * Errornous tests
                 */
                // Implicit start and end time should not be added as there must be a space infront of the from
                { " #{s}from 2pm to 4pm", null }

        });
    }
    //@formatter:on
}
