package w094j.ctrl8.pojo;

import java.util.Date;
import java.util.LinkedList;

import org.bson.types.ObjectId;

import w094j.ctrl8.parse.statement.Statement;

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

    private String id;
    private String title;
    private String category;
    private String description;
    private Date startDate;
    private Date endDate;
    private String location;
    private Integer priority;
    private Date reminder;
    private TaskType taskType;
    private Boolean isDone;
    private Date lastModifiedTime;
    private Boolean isSynced;
    private String googleId;
    private String eTag;
    private LinkedList<Statement> statementHistory;

    /**
     * default constructor
     */
    public Task() {
        this.taskType = TaskType.INCOMPLETE;
    }

    @Override
    public int compareTo(final Task task) {
        return this.title.compareTo(task.getTitle());
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
     * @return eTag
     */
    public String getETag() {
        return this.eTag;
    }

    /**
     * @return googleId
     */
    public String getGoogleId() {
        return this.googleId;
    }

    /**
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return true is task is synced with Google
     */
    public Boolean getIsSynced() {
        return this.isSynced;
    }

    /**
     * @return lastModifiedTime
     */
    public Date getLastModifiedTime() {
        return this.lastModifiedTime;
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
    public Integer getPriority() {
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
    public Boolean getStatus() {
        if (this.isDone == null) {
            return false;
        }
        return this.isDone;
    }

    /**
     * @return task type
     */
    public TaskType getTaskType() {
        this.changeTaskType();
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
        this.updateTimeAndSyncStatus();
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
        this.updateTimeAndSyncStatus();
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        this.updateTimeAndSyncStatus();
    }

    /**
     * Set eTag for Google Calendar and Google Task
     *
     * @param eTag
     */
    public void setETag(String eTag) {
        this.eTag = eTag;
        this.isSynced = true;
    }

    /**
     * Set googleId
     *
     * @param googleId
     */
    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(String location) {
        this.location = location;
        this.updateTimeAndSyncStatus();
    }

    /**
     * @param priority
     *            the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
        this.updateTimeAndSyncStatus();
    }

    /**
     * @param reminder
     *            the reminder to set
     */
    public void setReminder(Date reminder) {
        this.reminder = reminder;
        this.updateTimeAndSyncStatus();
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        this.updateTimeAndSyncStatus();
    }

    /**
     * @param statementHistory
     *            the statementHistory to set
     */
    public void setStatementHistory(LinkedList<Statement> statementHistory) {
        this.statementHistory = statementHistory;
        this.updateTimeAndSyncStatus();
    }

    /**
     * @param isDone
     *            the isDone to set
     */
    public void setStatus(boolean isDone) {
        this.isDone = isDone;
        this.updateTimeAndSyncStatus();
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
        this.updateTimeAndSyncStatus();
    }

    /**
     * Change incomplete task to complete task this.task should have a title
     */
    public void toCompleteTask() {
        if (this.id == null) {
            this.id = new ObjectId().toString();
        }

        if ((this.title != null) && this.title.equals("")) {
            this.title = null;
        }

        if ((this.location != null) && this.location.equals("")) {
            this.location = null;
        }

        if ((this.startDate != null) && (this.endDate != null)) {
            if (this.startDate.after(this.endDate)) {
                this.startDate = null;
                this.endDate = null;
            }
        }

        if ((this.category != null) && this.category.equals("")) {
            this.category = null;
        }

        if ((this.description != null) && this.description.equals("")) {
            this.description = null;
        }

        this.changeTaskType();
        this.updateTimeAndSyncStatus();
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

            if (incompleteTask.title != null) {
                if (incompleteTask.title == "") {
                    this.title = null;
                } else {
                    this.title = incompleteTask.title;
                }
            }

            if (incompleteTask.location != null) {
                if (incompleteTask.location == "") {
                    this.location = null;
                } else {
                    this.location = incompleteTask.location;
                }
            }

            if (incompleteTask.startDate != null) {
                this.startDate = incompleteTask.startDate;
            }

            if (incompleteTask.endDate != null) {
                this.endDate = incompleteTask.endDate;
            }

            if (incompleteTask.category != null) {
                if (incompleteTask.category == "") {
                    this.category = null;
                } else {
                    this.category = incompleteTask.category;
                }
            }

            if (incompleteTask.description != null) {
                if (incompleteTask.description == "") {
                    this.description = null;
                } else {
                    this.description = incompleteTask.description;
                }
            }

            if (incompleteTask.reminder != null) {
                this.reminder = incompleteTask.reminder;
            }

            if (incompleteTask.priority != null) {
                this.priority = incompleteTask.priority;
            }

            if (incompleteTask.isDone != null) {
                this.isDone = incompleteTask.isDone;
            }

            this.changeTaskType();
            this.updateTimeAndSyncStatus();
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
    private void changeTaskType() {
        if ((this.startDate == null) && (this.endDate == null)) {
            this.taskType = TaskType.FLOATING;
        } else if ((this.startDate == null) && (this.endDate != null)) {
            this.taskType = TaskType.DEADLINE;
        } else if ((this.startDate != null) && (this.endDate == null)) {
            this.endDate = this.startDate;
            this.startDate = null;
            this.taskType = TaskType.DEADLINE;
        } else {
            this.taskType = TaskType.TIMED;
        }
    }

    private void updateTimeAndSyncStatus() {
        this.lastModifiedTime = new Date();
        if (this.isSynced != null) {
            this.isSynced = false;
        }
    }

}