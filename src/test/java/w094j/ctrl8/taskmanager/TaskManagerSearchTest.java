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
import w094j.ctrl8.database.config.TaskManagerConfig;
import w094j.ctrl8.pojo.Task;

/**
 * Tests the search functionality of the task mananger.
 */
@RunWith(value = Parameterized.class)
public class TaskManagerSearchTest {

    private static ITaskManager taskManager;

    private String[] objectIdExpected;
    private String searchQuery;

    /**
     * Creates a test case with the search query, and retrieve the array of
     * objectIds expected
     *
     * @param searchQuery
     * @param objectIdExpected
     */
    public TaskManagerSearchTest(String searchQuery, String[] objectIdExpected) {
        this.searchQuery = searchQuery;
        this.objectIdExpected = objectIdExpected;
    }

    /**
     * @return test data.
     */
    @SuppressWarnings("deprecation")
    @Parameters(name = "{index}: Search \"{0}\" to get objectIds({1})")
    //@formatter:off
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /**
                 * Normal tests
                 */
                { "I am", new String[] {"0","1","2","3"} },
                { "A", new String[] {"4"} },


        });
    }

    // @formatter:on
    @BeforeClass
    public static void initialize() {
        TaskManagerConfig config = new TaskManagerConfig();
        AliasData aliasData = new AliasData();
        TaskData taskData = new TaskData();
        taskManager = new TaskManager(config, aliasData, taskData);

        Task fatherTask = new Task();
        fatherTask.setId("0");
        fatherTask.setTitle("I am your father.");
        fatherTask.setCategory("NUS");
        taskData.updateTaskMap(fatherTask, null, false);

        Task motherTask = new Task();
        motherTask.setId("1");
        motherTask.setTitle("I am your mother.");
        motherTask.setCategory("NUS");
        taskData.updateTaskMap(motherTask, null, false);

        Task sisterTask = new Task();
        sisterTask.setId("2");
        sisterTask.setTitle("I am your sister.");
        sisterTask.setCategory("FAM");
        taskData.updateTaskMap(sisterTask, null, false);

        Task brotherTask = new Task();
        brotherTask.setId("3");
        brotherTask.setTitle("I am your brother.");
        brotherTask.setCategory("FAM");
        taskData.updateTaskMap(brotherTask, null, false);

        Task aTask = new Task();
        aTask.setId("4");
        aTask.setTitle("A");
        aTask.setCategory("FAM");
        taskData.updateTaskMap(aTask, null, false);
    }

    /**
     * Tests the parsing of priority parameter.
     */
    @Test
    public void testDeadline() {

        assertArrayEquals(this.taskManager.search(this.searchQuery),
                this.objectIdExpected);
    }

}
