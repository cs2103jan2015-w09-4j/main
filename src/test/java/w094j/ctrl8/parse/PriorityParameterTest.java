//@author A0065517A
package w094j.ctrl8.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import w094j.ctrl8.database.config.ParameterConfig;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.statement.parameter.ParameterContainer;
import w094j.ctrl8.pojo.Task;

/**
 * Tests parsing of the priority parameter.
 */
@RunWith(value = Parameterized.class)
public class PriorityParameterTest {

    private static ParameterParser parser;
    private Integer expectedPriority;

    private String input;

    /**
     * Creates a Priority Parameter test.
     *
     * @param input
     *            Priority parameter to parse.
     * @param expectedPriority
     *            Expected Priority.
     */
    public PriorityParameterTest(String input, Integer expectedPriority) {
        this.input = input;
        this.expectedPriority = expectedPriority;
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
                { "%{}", 0 }, { "%{0}", 0 }, { "%{1}", 1 },
                { "%{2}", 2 }, { "%{3}", 3 }, { "%{4}", 4 },
                { "%{5}", 5 }, { "%{6}", 6 }, { "%{7}", 7 },
                { "%{8}", 8 }, { "%{9}", 9 }, { "%{10}", 10 }
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
     * Tests the parsing of priority parameter.
     */
    @Test
    public void test() {

        ParameterContainer parameterContainer;
        try {
            parameterContainer = parser.parse(this.input);
            Task task = new Task();
            parameterContainer.addAll(null, task);
            assertEquals(this.expectedPriority, task.getPriority());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertFalse(true);
        }

    }
}
