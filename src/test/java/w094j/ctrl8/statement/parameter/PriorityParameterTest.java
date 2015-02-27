//@author A0065517A
package w094j.ctrl8.statement.parameter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import w094j.ctrl8.pojo.Task;

/**
 * Tests parsing of the priority parameter.
 */
@RunWith(value = Parameterized.class)
public class PriorityParameterTest {

    private int expectedPriority;
    private String input;

    /**
     * Creates a Priority Parameter test.
     *
     * @param input
     *            Priority parameter to parse.
     * @param expectedPriority
     *            Expected Priority.
     */
    public PriorityParameterTest(String input, int expectedPriority) {
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

    // @formatter:on

    /**
     * Tests the parsing of priority parameter.
     */
    @Test
    public void test() {

        ParameterContainer parameterContainer = ParameterSymbol
                .parse(this.input);
        Task task = new Task();
        parameterContainer.addAll(null, task);
        assertEquals(this.expectedPriority, task.getPriority());
    }
}
