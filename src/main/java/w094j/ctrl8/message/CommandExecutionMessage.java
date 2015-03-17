//@author A0110787A
package w094j.ctrl8.message;

/**
 * Class contains all messages used by Command-related functions of
 * Terminal.java
 */
public class CommandExecutionMessage {
    public static final String EXCEPTION_BAD_TASKID = "TaskID does not exist in TaskMap";
    public static final String EXCEPTION_IS_INCOMPLETE_TASK = "TaskType=INCOMPLETE";
    public static final String EXCEPTION_MISSING_TASK = "Database does not contain Task";
    public static final String EXCEPTION_NULL_TASK = "Task Object not initialised";
    public static final String EXCEPTION_UPDATE_TASK_MAP = "Update TaskMap Error";
}
