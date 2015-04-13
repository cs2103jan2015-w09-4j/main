//@author A0065517A
package w094j.ctrl8.taskmanager;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.data.TaskData;
import w094j.ctrl8.database.config.ParserConfig;
import w094j.ctrl8.database.config.TaskManagerConfig;
import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.pojo.Task;

/**
 * Tests the search functionality of the task mananger.
 */
@RunWith(value = Parameterized.class)
public class TaskManagerSearchTest {

    private static Task aTask = new Task();
    private static Task brotherTask = new Task();
    private static Task fatherTask = new Task();
    private static Task motherTask = new Task();
    private static Task sisterTask = new Task();

    private static TaskData taskData = new TaskData();
    private String searchQuery;
    private Task[] taskArrayExpected;

    /**
     * Creates a test case with the search query, and retrieve the array of
     * objectIds expected
     *
     * @param searchQuery
     * @param taskArrayExpected
     */
    public TaskManagerSearchTest(String searchQuery, Task[] taskArrayExpected) {
        this.searchQuery = searchQuery;
        this.taskArrayExpected = taskArrayExpected;
        Arrays.sort(this.taskArrayExpected);
    }

    /**
     * @return test data.
     */
    @SuppressWarnings("deprecation")
    @Parameters(name = "{index}: Search \"{0}\" to get objectIds({1})")
    // @formatter:off
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /**
                 * Normal tests
                 */
                { "I am", new Task[] { fatherTask, motherTask, sisterTask, brotherTask } },
                { "A", new Task[] { aTask } },
                { "a", new Task[] { aTask } }

        });
    }

    // @formatter:on
    @BeforeClass
    public static void initialize() {
        Parser.initInstance(new ParserConfig(), new AliasData());

        TaskManagerConfig config = new TaskManagerConfig();
        AliasData aliasData = new AliasData();
        TaskManager.initInstance(config, aliasData, taskData);

        fatherTask.setTitle("I am your father.");
        fatherTask.setCategory("NUS");
        taskData.updateTaskMap(fatherTask, null, false);

        motherTask.setTitle("I am your mother.");
        motherTask.setCategory("NUS");
        taskData.updateTaskMap(motherTask, null, false);

        sisterTask.setTitle("I am your sister.");
        sisterTask.setCategory("FAM");
        taskData.updateTaskMap(sisterTask, null, false);

        brotherTask.setTitle("I am your brother.");
        brotherTask.setCategory("FAM");
        brotherTask.setDescription("NUS");
        taskData.updateTaskMap(brotherTask, null, false);

        aTask.setTitle("A NUS");
        aTask.setCategory("FAM");
        taskData.updateTaskMap(aTask, null, false);
    }

    /**
     * Tests the parsing of priority parameter.
     *
     * @throws DataException
     */
    @Test
    public void testDeadline() throws DataException {

        assertArrayEquals(taskData.search(this.searchQuery),
                this.taskArrayExpected);
    }

}
