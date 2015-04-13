package w094j.ctrl8.pojo;

import java.util.Date;

import org.bson.types.ObjectId;

import w094j.ctrl8.exception.DataException;

/**
 * Class encapsulates a task object, which contains all the information required
 * for a task. This includes the following: Task title, start/end date, priority
 * level TODO: Additional support for custom priority
 */

// @author A0112521B

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

    private String category;
    private String description;
    private Date endDate;
    private String etag;
    private String googleId;
    //@formatter:off
    private ObjectId id;
    private Boolean isDone;

    private Boolean isSynced;

    private Date lastModifiedTime;
    private String location;
    private Integer priority;
    private Date reminder;
    private Date startDate;
    private TaskType taskType;
    //@formatter:on
    private String title;

    /**
     * default constructor
     */
    public Task() {
        this.taskType = TaskType.INCOMPLETE;
        this.id = new ObjectId();
    }

    public Task(Task task) {
        this.category = task.category;
        this.description = task.description;
        this.endDate = task.endDate == null ? null : (Date) task.endDate
                .clone();
        this.etag = task.etag;
        this.googleId = task.googleId;
        // Cloned task will share same ID object
        this.id = task.id;
        this.isDone = task.isDone;
        this.isSynced = task.isSynced;
        this.lastModifiedTime = task.lastModifiedTime == null ? null
                : (Date) task.lastModifiedTime.clone();
        this.location = task.location;
        this.priority = task.priority;
        this.reminder = task.reminder == null ? null : (Date) task.reminder
                .clone();
        this.startDate = task.startDate == null ? null : (Date) task.startDate
                .clone();
        this.taskType = task.taskType;
        this.title = task.title;
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
        return this.endDate;
    }

    /**
     * @return eTag
     */
    public String getEtag() {
        return this.etag;
    }

    /**
     * @return googleId
     */
    public String getGoogleId() {
        return this.googleId;
    }

    /**
     * @return the id
     */
    public ObjectId getId() {
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
        return this.startDate;
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
    public void setEtag(String eTag) {
        this.etag = eTag;
        this.isSynced = true;
        this.lastModifiedTime = new Date();
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
     * @param id
     *            the id to set
     */
    public void setId(ObjectId id) {
        this.id = id;
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
    public void setPriority(Integer priority) {
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
     * @param isDone
     *            the isDone to set
     */
    public void setStatus(boolean isDone) {
        this.isDone = isDone;
        this.updateTimeAndSyncStatus();
    }

    /**
     * @param isSynced
     *            the isSynced to set
     */
    public void setSyncStatus(Boolean isSynced) {
        this.isSynced = isSynced;
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
     * 
     * @throws DataException
     */
    public void toCompleteTask() throws DataException {

        if ((this.title == null) || this.title.equals("")) {
            throw new DataException("No title");
        }

        if ((this.location != null) && this.location.equals("")) {
            this.location = null;
        }

        if ((this.startDate != null) && (this.endDate != null)) {
            if (this.startDate.after(this.endDate)) {
                throw new DataException(
                        "Start Date shouldn't be after End Date");
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
