//@author A0112092W
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
import w094j.ctrl8.parse.statement.parameter.LocationParameter;
import w094j.ctrl8.parse.statement.parameter.Parameter;
import w094j.ctrl8.parse.statement.parameter.PriorityParameter;
import w094j.ctrl8.parse.statement.parameter.ReminderParameter;
import w094j.ctrl8.parse.statement.parameter.StartTimeParameter;
import w094j.ctrl8.parse.statement.parameter.TitleParameter;

/**
 * Tests parsing of the explicit long parameters to its String representation.
 * Tests will not include test explicit short and implicit parameters.
 */
@RunWith(value = Parameterized.class)
public class ExplicitLongParameterTest extends ParameterTest {

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
    public ExplicitLongParameterTest(String input, List<Parameter> expected)
            throws ParseException {
        super(input, expected, false, false);
    }

    /**
     * @return test data.
     */
    @Parameters(name = "{index}: Parse \"{0}\" to get all the parameters({1})")
    //@formatter:off
    public static Iterable<Object[]> data() {
        beforeClass();
        return Arrays.asList(new Object[][] {
                /**
                 * Normal tests
                 */
                // Test all the different tags
                { "#{nus}", Arrays.asList(new Parameter[] {new CategoryParameter("nus")}) },
                { "-{today 1pm}", Arrays.asList(new Parameter[] {new DeadlineParameter("today 1pm")}) },
                { "+{Remember to talk about Crawler technologies.}", Arrays.asList(new Parameter[] {new DescriptionParameter("Remember to talk about Crawler technologies.")}) },
                { "@{Prof. Martin Henz Office}", Arrays.asList(new Parameter[] {new LocationParameter("Prof. Martin Henz Office")}) },
                { "%{10}", Arrays.asList(new Parameter[] {new PriorityParameter("10")}) },
                { "!{today 12pm}", Arrays.asList(new Parameter[] {new ReminderParameter("today 12pm")}) },
                { "~{today 3pm}", Arrays.asList(new Parameter[] {new StartTimeParameter("today 3pm")}) },
                { "={UROP Meeting}", Arrays.asList(new Parameter[] {new TitleParameter("UROP Meeting")}) },
                // Test the empty parameter
                { "#{}", Arrays.asList(new Parameter[] {new CategoryParameter("")}) },
                // Tags with escaped Parameter in the payload
                { "={UROP \\=Meeting} #{nus}", Arrays.asList(new Parameter[] {new TitleParameter("UROP =Meeting"), new CategoryParameter("nus") }) },
                // Empty String to empty list
                { "", Arrays.asList(new Parameter[]{})},
                /**
                 * Extreme test cases
                 */
                //All tags in one time
                {"={OP2 Meeting} -{tmr 3pm} +{Prepare for own slides} @{biz canteen} %{10} !{tmr 12pm} ~{tmr 2pm} #{cs2101}",
                    Arrays.asList(new Parameter[] {new TitleParameter("OP2 Meeting"), new DeadlineParameter("tmr 3pm"),
                            new DescriptionParameter("Prepare for own slides"),new LocationParameter("biz canteen"),new PriorityParameter("10"),
                            new ReminderParameter("tmr 12pm"), new StartTimeParameter("tmr 2pm"), new CategoryParameter("cs2101") })
                },
                /**
                 * Errorneous test cases
                 */
                //error parameter
                {"==", null},
                //wrong usage of brackets
                {"=(meeting)", null},
                //multiple usage of tags in 1 field
                {"@#{friends}", null},
                //random parameter
                {".{today}", null},
                //random parameter
                {"*{tmr}", null},
                // Multi-tags is an error
                {"={Meeting1} ={Meeting2} ={Meeting3} ={Meeting4} ={Meeting5} ={Meeting6} ={Meeting7} ={Meeting8} ={Meeting9} ={Meeting10} ={Meeting11}",
                    null
                }

        });
    }
    // @formatter:on

}
