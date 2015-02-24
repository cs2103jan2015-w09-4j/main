package w094j.ctrl8.pojo;

import java.util.Date;
import java.util.LinkedList;

import w094j.ctrl8.statement.Statement;

/**
 * Class encapsulates a task object, which contains all the information required
 * for a task. This includes the following: Task title, start/end date, priority
 * level TODO: Additional support for custom priority
 */

// @author A0065517A
// @author A0110787
// @author A0112521B

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
         * Tasks with no title. This is for update purpose.
         */
        INCOMPLETE,
        /**
         * Tasks with a start time and end time. Also known as events.
         */
        TIMED
    }

    /* Global parameters for all tasks */
    private static final int DEFAULT_PRIORITY = 0;

    /* Variables */
    private String category;
    private String description;
    private Date endDate;
    private boolean isDone;
    private String location;
    private int priority;
    private Date reminder;
    private Date startDate;

    /*
     * Full Statement history of the particular Task.
     */
    private LinkedList<Statement> statementHistory;
    private TaskType taskType;
    private String title;

    /**
     * Constructor for Floating tasks
     *
     * @param inputTaskTitle
     * @param inputPriority
     */
    public Task(String inputTaskTitle, int inputPriority, Statement st) {
        this.taskType = TaskType.FLOATING;
        this.title = inputTaskTitle;
        this.priority = inputPriority;
        this.statementHistory = new LinkedList<Statement>();
        this.statementHistory.addLast(st);
    }

    /**
     * Constructor for Timed tasks
     *
     * @param inputTaskTitle
     * @param inputStartDate
     * @param inputEndDate
     * @param inputPriority
     */
    private Task(String inputTaskTitle, Date inputStartDate, Date inputEndDate,
            int inputPriority, Statement st) {
        this.taskType = TaskType.TIMED;
        this.title = inputTaskTitle;
        this.startDate = inputStartDate;
        this.endDate = inputEndDate;
        this.priority = inputPriority;
        this.statementHistory = new LinkedList<Statement>();
        this.statementHistory.addLast(st);
    }

    /**
     * Constructor for Incomplete tasks
     *
     * @param inputLocation
     * @param inputStartDate
     * @param inputEndDate
     * @param inputCategory
     * @param inputReminder
     * @param inputPriority
     * @param inputStatus
     */
    private Task(String inputLocation, Date inputStartDate, Date inputEndDate,
            String inputCategory, Date inputReminder, int inputPriority,
            boolean inputStatus, Statement st) {
        this.taskType = TaskType.INCOMPLETE;
        this.location = inputLocation;
        this.startDate = inputStartDate;
        this.endDate = inputEndDate;
        this.category = inputCategory;
        this.reminder = inputReminder;
        this.priority = inputPriority;
        this.isDone = inputStatus;
        this.statementHistory = new LinkedList<Statement>();
        this.statementHistory.addLast(st);
    }

    /**
     * Constructor for Deadline tasks
     *
     * @param inputTaskTitle
     * @param inputDeadlineDate
     * @param inputPriority
     */
    private Task(String inputTaskTitle, Date inputDeadlineDate,
            int inputPriority, Statement st) {
        this.taskType = TaskType.DEADLINE;
        this.title = inputTaskTitle;
        this.endDate = inputDeadlineDate;
        this.priority = inputPriority;
        this.statementHistory = new LinkedList<Statement>();
        this.statementHistory.addLast(st);
    }

    public static Task createNewTask(Statement st) {
        // TODO: refer to @author Eric for how to access statements
        return null;
    }

    private static Task createDeadlineTask(String inputTaskTitle,
            Date inputDeadlineDate, int inputPriority, Statement st) {
        return new Task(inputTaskTitle, inputDeadlineDate, inputPriority, st);
    }

    private static Task createDeadlineTask(String inputTaskTitle,
            Date inputDeadlineDate, Statement st) {
        return new Task(inputTaskTitle, inputDeadlineDate, DEFAULT_PRIORITY, st);
    }

    private static Task createFloatingTask(String inputTaskTitle,
            int inputPriority, Statement st) {
        return new Task(inputTaskTitle, inputPriority, st);
    }

    private static Task createFloatingTask(String inputTaskTitle, Statement st) {
        return new Task(inputTaskTitle, DEFAULT_PRIORITY, st);
    }

    private static Task createIncompleteTask(String inputLocation,
            Date inputStartDate, Date inputEndDate, String inputCategory,
            Date inputReminder, int inputPriority, boolean inputStatus,
            Statement st) {
        return new Task(inputLocation, inputStartDate, inputEndDate,
                inputCategory, inputReminder, inputPriority, inputStatus, st);
    }

    private static Task createTimedTask(String inputTaskTitle,
            Date inputStartDate, Date inputEndDate, int inputPriority,
            Statement st) {
        return new Task(inputTaskTitle, inputStartDate, inputEndDate,
                inputPriority, st);
    }

    private static Task createTimedTask(String inputTaskTitle,
            Date inputStartDate, Date inputEndDate, Statement st) {
        return new Task(inputTaskTitle, inputStartDate, inputEndDate,
                DEFAULT_PRIORITY, st);
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * @return Date :the end Date of the task
     */
    public Date getEndDate() {
        // Only timed and deadline tasks have an end date
        assert ((this.taskType == TaskType.TIMED) || (this.taskType == TaskType.DEADLINE));
        return this.endDate;
    }

    /**
     * @return location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @return priority
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * @return date of the reminder
     */
    public Date getReminder() {
        return this.reminder;
    }

    /**
     * @return Date :the start Date of the task
     */
    public Date getStartDate() {
        // only Timed tasks have a start date
        assert (this.taskType == TaskType.TIMED);
        return this.startDate;
    }

    /**
     * @return the statementHistory
     */
    public LinkedList<Statement> getStatementHistory() {
        return this.statementHistory;
    }

    /**
     * @return status
     */
    public boolean getStatus() {
        return this.isDone;
    }

    /**
     * @return title
     */
    public String getTaskTitle() {
        return this.title;
    }

    /**
     * @return task type
     */
    public TaskType getTaskType() {
        return this.taskType;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @param priority
     *            the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @param reminder
     *            the reminder to set
     */
    public void setReminder(Date reminder) {
        this.reminder = reminder;
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
     * @param isDone
     *            the isDone to set
     */
    public void setStatus(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTaskTitle(String title) {
        this.title = title;
    }

    /**
     * @param taskType
     *            the taskType to set
     */
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    /**
     * @param incompleteTask
     */
    public void update(Task incompleteTask) {
        this.priority = incompleteTask.priority;
        this.isDone = incompleteTask.isDone;

        if (incompleteTask.category != null) {
            if (incompleteTask.category == "") {
                this.category = null;
            } else {
                this.category = incompleteTask.category;
            }
        }
        if (incompleteTask.location != null) {
            if (incompleteTask.location == "") {
                this.location = null;
            } else {
                this.location = incompleteTask.location;
            }
        }

        if (incompleteTask.startDate != new Date()) {
            this.startDate = incompleteTask.startDate;
        }
        if (incompleteTask.endDate != new Date()) {
            this.endDate = incompleteTask.endDate;
        }
        if (incompleteTask.reminder != new Date()) {
            this.reminder = incompleteTask.reminder;
        }

        if (this.endDate == new Date()) {
            this.taskType = TaskType.FLOATING;
        } else if (this.startDate == new Date()) {
            this.taskType = TaskType.DEADLINE;
        } else {
            this.taskType = TaskType.TIMED;
        }

        this.statementHistory
                .addLast(incompleteTask.statementHistory.getLast());
    }

}
