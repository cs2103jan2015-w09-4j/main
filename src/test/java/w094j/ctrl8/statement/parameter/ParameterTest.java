//@author A0065517A
package w094j.ctrl8.statement.parameter;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.ParameterConfig;
import w094j.ctrl8.parse.ParameterParser;

/**
 * Tests parsing of the explicit parameters to its String representation. This
 * will not test contextual parsing.
 */
@RunWith(value = Parameterized.class)
public class ParameterTest {
    private static Logger logger = LoggerFactory.getLogger(ParameterTest.class);
    private static ParameterParser parser;
    private ParameterContainer expectedParamContainer;
    private String input;

    /**
     * Creates a Explicit Parameter test.
     *
     * @param input
     *            The parameter(s) to be parsed.
     * @param expected
     *            Expected Parsed result.
     */
    public ParameterTest(String input, List<Parameter> expected) {
        this.input = input;
        this.expectedParamContainer = new ParameterContainer(expected);
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
                { "#{nus}", Arrays.asList(new Parameter[] {new CategoryParameter("nus")}) },
                { "-{today 1pm}", Arrays.asList(new Parameter[] {new DeadlineParameter("today 1pm")}) },
                { "+{Remember to talk about Crawler technologies.}", Arrays.asList(new Parameter[] {new DescriptionParameter("Remember to talk about Crawler technologies.")}) },
                { "@{Prof. Martin Henz Office}", Arrays.asList(new Parameter[] {new LocationParameter("Prof. Martin Henz Office")}) },
                { "%{10}", Arrays.asList(new Parameter[] {new PriorityParameter("10")}) },
                { "!{today 12pm}", Arrays.asList(new Parameter[] {new ReminderParameter("today 12pm")}) },
                { "~{today 3pm}", Arrays.asList(new Parameter[] {new StartTimeParameter("today 3pm")}) },
                { "={UROP Meeting}", Arrays.asList(new Parameter[] {new TitleParameter("UROP Meeting")}) },
                /**
                 * Normal tests: Accept empty parameter
                 */
                { "#{}", Arrays.asList(new Parameter[] {new CategoryParameter("")}) },
                /**
                 * Normal tests: Multi-Tags tests
                 */
                { "={UROP Meeting} ={UROP Meetings}", Arrays.asList(new Parameter[] {new TitleParameter("UROP Meeting"), new TitleParameter("UROP Meetings")}) },
                { "={UROP Meeting}#{nus}", Arrays.asList(new Parameter[] {new TitleParameter("UROP Meeting"), new CategoryParameter("nus") }) },
                
                //@author A0112092W 
                /**
                 * Errorneous test cases
                 */
                //null parameter
                { "", null},
                //error parameter
                {"==", null},
                //wrong usage of brackets
                {"=(meeting)",null},
                //multiple usage of tags in 1 field
                {"@#{friends}", null},
                //random parameter
                {".{today}",null},
                //random parameter
                {"*{tmr}", null},
                //duplicate parameter
                
                
                /**
                 * Extreme test cases
                 */
                //All tags in one time
                {"={OP2 Meeting} -{tmr 3pm} +{Prepare for own slides} @{biz canteen} %{10} !{tmr 12pm} ~{tmr 2pm} #{cs2101}", 
                    Arrays.asList(new Parameter[] {new TitleParameter("OP2 Meeting"), new DeadlineParameter("tmr 3pm"),
                            new DescriptionParameter("Prepare for own slides"),new LocationParameter("biz canteen"),new PriorityParameter("10"),
                            new ReminderParameter("tmr 12pm"), new StartTimeParameter("tmr 2pm"), new CategoryParameter("cs2101") })
                },
                //Extreme multi-tags
                {"={Meeting1} ={Meeting2} ={Meeting3} ={Meeting4} ={Meeting5} ={Meeting6} ={Meeting7} ={Meeting8} ={Meeting9} ={Meeting10} ={Meeting11}",
                    Arrays.asList(new Parameter[] {new TitleParameter("Meeting1"),new TitleParameter("Meeting2"),new TitleParameter("Meeting3"),
                            new TitleParameter("Meeting4"),new TitleParameter("Meeting5"),new TitleParameter("Meeting6"),new TitleParameter("Meeting7"),
                            new TitleParameter("Meeting8"),new TitleParameter("Meeting9"),new TitleParameter("Meeting10"), new TitleParameter("Meeting11")})
                    
                }
                
                
        });
    }

    /**
     *
     */
    @BeforeClass
    public static void initParser() {
        ParameterConfig config = new ParameterConfig();
        parser = new ParameterParser(config);
    }

    // @formatter:on

    /**
     * Tests the parsing of the command text
     */
    @Test
    public void test() {

            assertTrue(this.expectedParamContainer.equals(parser.parse(this.input)));
        
    }
}
