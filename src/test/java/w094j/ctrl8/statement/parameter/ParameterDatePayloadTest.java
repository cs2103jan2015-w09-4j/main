//@author A0065517A
package w094j.ctrl8.statement.parameter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

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
                { "3 Aug 1992 00:00:00", new Date(92,7,3,0,0,0)  }
        });
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