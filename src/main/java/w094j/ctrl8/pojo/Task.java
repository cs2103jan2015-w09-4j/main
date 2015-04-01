package w094j.ctrl8.pojo;

import java.util.Date;
import java.util.LinkedList;

import w094j.ctrl8.statement.Statement;

/**
 * Class encapsulates a task object, which contains all the information required
 * for a task. This includes the following: Task title, start/end date, priority
 * level TODO: Additional support for custom priority
 */

//@author A0065517A
//@author A0110787A
//@author A0112521B

public class Task implements Comparable<Task> {

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

    private static final int CATEGORY = 5;

    /* Global parameters for all tasks */
    private static final int DEFAULT_PRIORITY = 0;
    private static final int DESCRIPTION = 6;
    private static final int ENDDATE = 4;
    /* Global parameters for isSet boolean array */
    private static final int IS_COMPLETE = 0;
    private static final int ISSET_SIZE = 10;
    private static final int LOCATION = 2;
    private static final int PRIORITY = 8;
    private static final int REMINDER = 7;
    private static final int STARTDATE = 3;
    private static final int STATUS = 9;
    private static final int TITLE = 1;

    /* Variables */
    private String category;
    private String description;
    private Date endDate;
    private boolean isDone;
    private boolean isSet[];
    private String location;
    private int priority;
    private Date reminder;
    private Date startDate;
    private LinkedList<Statement> statementHistory;
    private TaskType taskType;
    private String title;

    /**
     * default constructor
     */
    public Task() {
        this.isSet = new boolean[ISSET_SIZE];
        this.taskType = TaskType.INCOMPLETE;
        this.priority = DEFAULT_PRIORITY;
    }

    @Override
    public int compareTo(final Task task) {
        return this.title.compareTo(task.getTitle());
    }

    /**
     * @param task
     * @return true if the tasks are the same
     */
    public boolean equals(Task task) {
        return this.title.equals(task.getTitle())
                && (((this.location == null) && (task.location == null)) || this.location
                        .equals(task.location))
                && (((this.startDate == null) && (task.startDate == null)) || (this.startDate == task.startDate))
                && (((this.endDate == null) && (task.endDate == null)) || (this.endDate == task.endDate))
                && (((this.category == null) && (task.category == null)) || this.category
                        .equals(task.category))
                && (((this.description == null) && (task.description == null)) || this.description
                        .equals(task.description))
                && (((this.reminder == null) && (task.reminder == null)) || (this.reminder == task.reminder))
                && (this.priority == task.priority)
                && (this.isDone == task.isDone);
    }

    /**
     * @return category
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return end date
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
     * @return task type
     */
    public TaskType getTaskType() {
        if (this.isSet[IS_COMPLETE]) {
            this.changeTaskType();
        }
        return this.taskType;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
        this.isSet[CATEGORY] = true;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
        this.isSet[DESCRIPTION] = true;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        this.isSet[ENDDATE] = true;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(String location) {
        this.location = location;
        this.isSet[LOCATION] = true;
    }

    /**
     * @param priority
     *            the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
        this.isSet[PRIORITY] = true;
    }

    /**
     * @param reminder
     *            the reminder to set
     */
    public void setReminder(Date reminder) {
        this.reminder = reminder;
        this.isSet[REMINDER] = true;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        this.isSet[STARTDATE] = true;
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
        this.isSet[STATUS] = true;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
        this.isSet[TITLE] = true;
    }

    /**
     * Change incomplete task to complete task this.task should have a title
     *
     * @return true if it is changed to a complete task successfully
     * @throws Exception
     */
    public boolean toCompleteTask() {

        if (this.isSet[LOCATION] && this.location.equals("")) {
            this.location = null;
            this.isSet[LOCATION] = false;
        }

        if (this.isSet[STARTDATE] && (this.startDate == null)) {
            this.isSet[STARTDATE] = false;
        }

        if (this.isSet[ENDDATE] && (this.endDate == null)) {
            this.isSet[ENDDATE] = false;
        }

        if (this.isSet[STARTDATE] && this.isSet[ENDDATE]) {
            if (this.startDate.after(this.endDate)) {
                this.isSet[STARTDATE] = false;
                this.isSet[ENDDATE] = false;
                this.startDate = null;
                this.endDate = null;
            }

        }

        if (this.isSet[CATEGORY] && this.category.equals("")) {
            this.category = null;
            this.isSet[CATEGORY] = false;
        }

        if (this.isSet[DESCRIPTION] && this.description.equals("")) {
            this.description = null;
            this.isSet[DESCRIPTION] = false;
        }

        if (this.isSet[REMINDER] && (this.reminder == null)) {
            this.isSet[REMINDER] = false;
        }

        this.isSet[PRIORITY] = true;
        this.isSet[STATUS] = true;

        return this.changeTaskType();

    }

    /**
     * Updates the current task and changes it to a complete task. this.task
     * should not be an incomplete task.
     *
     * @param incompleteTask
     * @return true if it updates successfully
     */
    public boolean update(Task incompleteTask) {
        if (this.taskType != TaskType.INCOMPLETE) {

            if (incompleteTask.isSet[TITLE] && !incompleteTask.title.equals("")) {
                this.title = incompleteTask.title;
            }

            if (incompleteTask.isSet[LOCATION]) {
                if (incompleteTask.location == "") {
                    this.location = null;
                    this.isSet[LOCATION] = false;
                } else {
                    this.location = incompleteTask.location;
                    this.isSet[LOCATION] = true;
                }
            }

            if (incompleteTask.isSet[STARTDATE]) {
                this.isSet[STARTDATE] = (incompleteTask.startDate == null) ? false
                        : true;
                this.startDate = incompleteTask.startDate;
            }

            if (incompleteTask.isSet[ENDDATE]) {
                this.isSet[ENDDATE] = (incompleteTask.endDate == null) ? false
                        : true;
                this.endDate = incompleteTask.endDate;
            }

            if (incompleteTask.isSet[CATEGORY]) {
                if (incompleteTask.category == "") {
                    this.category = null;
                    this.isSet[CATEGORY] = false;
                } else {
                    this.category = incompleteTask.category;
                    this.isSet[CATEGORY] = true;
                }
            }

            if (incompleteTask.isSet[DESCRIPTION]) {
                if (incompleteTask.description == "") {
                    this.description = null;
                    this.isSet[DESCRIPTION] = false;
                } else {
                    this.description = incompleteTask.description;
                    this.isSet[DESCRIPTION] = true;
                }
            }

            if (incompleteTask.isSet[REMINDER]) {
                this.isSet[REMINDER] = (incompleteTask.reminder == null) ? false
                        : true;
                this.reminder = incompleteTask.reminder;
            }

            if (incompleteTask.isSet[PRIORITY]) {
                this.priority = incompleteTask.priority;
            }
            this.isSet[PRIORITY] = true;

            if (incompleteTask.isSet[STATUS]) {
                this.isDone = incompleteTask.isDone;
            }
            this.isSet[STATUS] = true;

            this.changeTaskType();

//            this.statementHistory.addLast(incompleteTask.statementHistory
//                    .getLast());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Change Task Type (incomplete/deadline/floating/timed)
     */
    private boolean changeTaskType() {
        if (!this.isSet[TITLE] || (this.title == null) || this.title.equals("")) {
            this.taskType = TaskType.INCOMPLETE;
            this.isSet[IS_COMPLETE] = false;
            return false;
        } else if (!this.isSet[ENDDATE]) {
            this.startDate = null;
            this.isSet[STARTDATE] = false;
            this.taskType = TaskType.FLOATING;
        } else if (!this.isSet[STARTDATE] && this.isSet[ENDDATE]) {
            this.taskType = TaskType.DEADLINE;
        } else {
            this.taskType = TaskType.TIMED;
        }
        this.isSet[IS_COMPLETE] = true;
        return true;
    }

}