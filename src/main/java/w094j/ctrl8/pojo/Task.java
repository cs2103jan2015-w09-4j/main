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
    
    /* Global parameters for isSet boolean array */
    private static final int IS_COMPLETE = 0;
    private static final int TITLE = 1;
    private static final int LOCATION = 2;
    private static final int STARTDATE = 3;
    private static final int ENDDATE = 4;
    private static final int CATEGORY = 5;
    private static final int DESCRIPTION = 6;
    private static final int REMINDER = 7;
    private static final int PRIORITY = 8;
    private static final int STATUS = 9;
    private static final int ISSET_SIZE = 10;
    
    /* Global parameters for isRemoved boolean array */
    private static final int REMOVE_STARTDATE = 0;
    private static final int REMOVE_ENDDATE = 1;
    private static final int REMOVE_REMINDER = 2;
    private static final int ISREMOVED_SIZE = 3;

    /* Variables */
    private String category;
    private String description;
    private Date endDate;
    private boolean isDone;
    private boolean isSet[];
    private boolean isRemoved[];
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
        this.isRemoved = new boolean[ISREMOVED_SIZE];
        this.taskType = TaskType.INCOMPLETE;
        this.priority = DEFAULT_PRIORITY;
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
        if(this.isSet[IS_COMPLETE]){
            changeTaskType();
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
     * remove start date
     */
    public void removeStartDate() {
        this.isRemoved[REMOVE_STARTDATE] = true;
        this.isSet[STARTDATE] = false;
    }
    
    /**
     * remove end date
     */
    public void removeEndDate() {
        this.isRemoved[REMOVE_ENDDATE] = true;
        this.isSet[ENDDATE] = false;
    }
    
    /**
     * remove reminder
     */
    public void removeReminder() {
        this.isRemoved[REMOVE_REMINDER] = true;
        this.isSet[REMINDER] = false;
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

    /*
     * /**
     * @param taskType the taskType to set public void setTaskType(TaskType
     * taskType) { this.taskType = taskType; }
     */
    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
        this.isSet[TITLE] = true;
    }

    /**
     * @param incompleteTask
     */
    public void update(Task incompleteTask) {
        
        if(incompleteTask.isSet[LOCATION]){
            if (incompleteTask.location == "") {
                this.location = null;
                this.isSet[LOCATION] = false;
            } else {
                this.location = incompleteTask.location;
                this.isSet[LOCATION] = true;
            }
        }
        
        if(incompleteTask.isSet[STARTDATE]){
            this.startDate = incompleteTask.startDate;
            this.isSet[STARTDATE] = true;
        }
        
        if(incompleteTask.isRemoved[REMOVE_STARTDATE]){
            this.isSet[STARTDATE] = false;
        }
        
        if(incompleteTask.isSet[ENDDATE]){
            this.endDate = incompleteTask.endDate;
            this.isSet[ENDDATE] = true;
        }
        
        if(incompleteTask.isRemoved[REMOVE_ENDDATE]){
            this.isSet[ENDDATE] = false;
        }
        
        if (incompleteTask.isSet[CATEGORY]) {
            if (incompleteTask.category == "") {
                this.category = null;
            } else {
                this.category = incompleteTask.category;
            }
        }
        
        if (incompleteTask.isSet[DESCRIPTION]) {
            if (incompleteTask.description == "") {
                this.description = null;
            } else {
                this.description = incompleteTask.description;
            }
        }
        
        if(incompleteTask.isSet[REMINDER]){
            this.reminder = incompleteTask.reminder;
            this.isSet[REMINDER] = true;
        }
        
        if(incompleteTask.isRemoved[REMOVE_REMINDER]){
            this.isSet[REMINDER] = false;
        }
        
        if(incompleteTask.isSet[PRIORITY]){
            this.priority = incompleteTask.priority;
        }
        
        if(incompleteTask.isSet[STATUS]){
            this.isDone = incompleteTask.isDone;
        }        
       
        changeTaskType();

        this.statementHistory
                .addLast(incompleteTask.statementHistory.getLast());
    }
    
    /**
     *  Change incomplete task to complete task
     */
    public void toCompleteTask() {
        this.isSet[IS_COMPLETE] = true;
        
        if(this.isSet[LOCATION] && this.location.equals("")){
            this.isSet[LOCATION] = false;
        }
        
        if(this.isSet[CATEGORY] && this.category.equals("")){
            this.isSet[CATEGORY] = false;
        }
        
        if(this.isSet[DESCRIPTION] && this.description.equals("")){
            this.isSet[DESCRIPTION] = false;
        }
        
        changeTaskType();
    }
    
    /**
     *  Change Task Type (deadline/floating/timed)
     */
    private void changeTaskType(){
        if (this.isSet[STARTDATE] && this.isSet[ENDDATE]) {
            this.taskType = TaskType.TIMED;          
        } else if (this.isSet[ENDDATE]) {
            this.taskType = TaskType.DEADLINE;
        } else {
            this.taskType = TaskType.FLOATING;
        }
    }

}