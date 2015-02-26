//@author A0065517A
package w094j.ctrl8.statement.parameter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

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

        List<Parameter> parameterList = ParameterSymbol.parse(this.input);
        assert (parameterList.size() == 1);
        assert (parameterList.get(0) instanceof PriorityParameter);
        PriorityParameter priorityParameter = (PriorityParameter) parameterList
                .get(0);
        assertEquals(this.expectedPriority, priorityParameter.getPiority());
    }
}
