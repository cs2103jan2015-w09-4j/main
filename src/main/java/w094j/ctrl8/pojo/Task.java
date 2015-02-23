package w094j.ctrl8.pojo;

import java.util.Date;
import java.util.LinkedList;

import w094j.ctrl8.statement.Statement;

/**
 * Class encapsulates a task object, which contains all the information required
 * for a task. This includes the following: Task name, start/end date, priority
 * level TODO: Additional support for custom priority
 */

/**
 * @author Lin Chen-Hsin A0112521B
 * @author Rodson Chue Le Sheng(A0110787)
 * @author Han Liang Wee, Eric(A0065517A)
 */
public class Task {

    /**
     * Enumerates the current list of task types.
     */
    public enum TaskType {
        /**
         * Tasks with deadline, either with date only or date with time. Tasks
         * with only date deadline is assumed to expire at 23:59 hrs.
         */
        DEADLINE,
        /**
         * Tasks with no dateline at all.
         */
        FLOATING,
        /**
         * Tasks with a start time and end time. Also known as events.
         */
        TIMED
    }

    /* Global parameters for all tasks */
    private static final int DEFAULT_PRIORITY = 1;

    /* Variables */
    private Date endDate;
    private int priority;
    private Date startDate;

    /*
     * Full Statement history of the particular Task.
     */
    private LinkedList<Statement> statementHistory;

    private String taskName;

    private TaskType taskType;

    /**
     * Constructor for floating tasks
     *
     * @param inputTaskName
     * @param inputPriority
     */
    public Task(String inputTaskName, int inputPriority, Statement st) {
        this.statementHistory = new LinkedList<Statement>();
        this.statementHistory.addLast(st);
        this.taskType = TaskType.FLOATING;
        this.taskName = inputTaskName;
        this.priority = inputPriority;

    }

    /**
     * Constructor for Timed tasks
     *
     * @param inputTaskName
     * @param inputStartDate
     * @param inputEndDate
     * @param inputPriority
     */
    private Task(String inputTaskName, Date inputStartDate, Date inputEndDate,
            int inputPriority, Statement st) {
        this.statementHistory = new LinkedList<Statement>();
        this.statementHistory.addLast(st);
        this.taskType = TaskType.TIMED;
        this.taskName = inputTaskName;
        this.priority = inputPriority;
        this.startDate = inputStartDate;
        this.endDate = inputEndDate;
    }

    /**
     * Constructor for tasks with deadline
     *
     * @param inputTaskName
     * @param inputDeadlineDate
     * @param inputPriority
     */
    private Task(String inputTaskName, Date inputDeadlineDate,
            int inputPriority, Statement st) {
        this.statementHistory = new LinkedList<Statement>();
        this.statementHistory.addLast(st);
        this.taskType = TaskType.DEADLINE;
        this.taskName = inputTaskName;
        this.endDate = inputDeadlineDate;
        this.priority = inputPriority;
    }

    public static Task createNewTask(Statement st) {
        // TODO: refer to @author Eric for how to access statements
        return null;
    }

    private static Task createDeadlineTask(String inputTaskName,
            Date inputDeadlineDate, int inputPriority, Statement st) {
        return new Task(inputTaskName, inputDeadlineDate, inputPriority, st);
    }

    private static Task createDeadlineTask(String inputTaskName,
            Date inputDeadlineDate, Statement st) {
        return new Task(inputTaskName, inputDeadlineDate, DEFAULT_PRIORITY, st);
    }

    private static Task createFloatingTask(String inputTaskName,
            int inputPriority, Statement st) {
        return new Task(inputTaskName, inputPriority, st);
    }

    private static Task createFloatingTask(String inputTaskName, Statement st) {
        return new Task(inputTaskName, DEFAULT_PRIORITY, st);
    }

    private static Task createNormalTask(String inputTaskName,
            Date inputStartDate, Date inputEndDate, int inputPriority,
            Statement st) {
        return new Task(inputTaskName, inputStartDate, inputEndDate,
                inputPriority, st);
    }

    private static Task createNormalTask(String inputTaskName,
            Date inputStartDate, Date inputEndDate, Statement st) {
        return new Task(inputTaskName, inputStartDate, inputEndDate,
                DEFAULT_PRIORITY, st);
    }

    /**
     * @return Date :the end Date of the task
     */
    public Date getEndDate() {
        // Only normal and deadline tasks have an end date
        assert ((this.taskType == TaskType.TIMED) || (this.taskType == TaskType.DEADLINE));
        return this.endDate;
    }

    public int getPriority() {
        return this.priority;
    }

    public Date getStartDate() {
        // only Normal tasks have a start date
        assert (this.taskType == TaskType.TIMED);
        return this.startDate;
    }

    /**
     * @return the statementHistory
     */
    public LinkedList<Statement> getStatementHistory() {
        return this.statementHistory;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public TaskType getTaskType() {
        return this.taskType;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @param priority
     *            the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @param statementHistory
     *            the statementHistory to set
     */
    public void setStatementHistory(LinkedList<Statement> statementHistory) {
        this.statementHistory = statementHistory;
    }

    /**
     * @param taskName
     *            the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @param taskType
     *            the taskType to set
     */
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

}
