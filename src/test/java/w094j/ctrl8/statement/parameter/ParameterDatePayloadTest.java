//@author A0065517A
package w094j.ctrl8.statement.parameter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.database.config.ParserConfig;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.parse.statement.parameter.DeadlineParameter;
import w094j.ctrl8.parse.statement.parameter.ReminderParameter;
import w094j.ctrl8.parse.statement.parameter.StartTimeParameter;

/**
 * Tests parsing of the Dates in the parameters deadline, starttime and
 * reminder.
 */
@RunWith(value = Parameterized.class)
public class ParameterDatePayloadTest {

    private DeadlineParameter deadlineParameter;
    private Date expectedDate;
    private ReminderParameter reminderParameter;
    private StartTimeParameter startTimeParameter;

    /**
     * Creates a test to test the 3 parameters as specified above.
     *
     * @param inputParameterPayload
     *            Payload to parse for each parameter.
     * @param expectedDate
     */
    public ParameterDatePayloadTest(String inputParameterPayload,
            Date expectedDate) {
        this.deadlineParameter = new DeadlineParameter(inputParameterPayload);
        this.reminderParameter = new ReminderParameter(inputParameterPayload);
        this.startTimeParameter = new StartTimeParameter(inputParameterPayload);
        this.expectedDate = expectedDate;
    }

    /**
     * @return test data.
     */
    @SuppressWarnings("deprecation")
    @Parameters(name = "{index}: Parse \"{0}\" to get all the parameters({1})")
    //@formatter:off
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /**
                 * Normal tests
                 */
                { "3 Aug 1992 00:00:00", new Date(92, 7, 3, 0, 0, 0) },

                //@author A0112521B
                { "June 15 1992 6:01:00", new Date(92, 5, 15, 6, 1, 0) },
                { "30 january 1993 23:59", new Date(93, 0, 30, 23, 59, 0) },
                { "31 dec 1994 12:00", new Date(94, 11, 31, 12, 00, 0) }

        });
    }

    @BeforeClass
    public static void initParser() {
        Parser.initInstance(new ParserConfig(), new AliasData());
    }

    // @formatter:on

    /**
     * Tests the parsing of priority parameter.
     */
    @Test
    public void testDeadline() {

        assertEquals(this.expectedDate, this.deadlineParameter.getDate());
    }

    /**
     * Tests the parsing of priority parameter.
     */
    @Test
    public void testReminder() {

        assertEquals(this.expectedDate, this.reminderParameter.getDate());
    }

    /**
     * Tests the parsing of priority parameter.
     */
    @Test
    public void testStartTime() {

        assertEquals(this.expectedDate, this.startTimeParameter.getDate());
    }
}
