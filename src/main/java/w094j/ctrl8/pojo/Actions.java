package w094j.ctrl8.pojo;

import org.bson.types.ObjectId;

import w094j.ctrl8.parse.statement.Statement;

//@author A0112092W
/**
 * This class is the wrapper for the statement that executed and the task that
 * related to it.
 */
public class Actions implements Comparable<Actions> {
    private ObjectId id;
    private Statement statement;
    private ObjectId taskID;

    /**
     * Creates a new actions with same statement and id with action
     *
     * @param action
     */
    public Actions(Actions action) {
        this.statement = action.getStatement();
        this.id = action.getID();
        this.setTaskID(action.getTaskID());
    }

    /**
     * @param statement
     * @param taskID
     */
    public Actions(Statement statement, ObjectId taskID) {
        this.statement = statement;
        this.id = new ObjectId();
        this.setTaskID(taskID);
    }

    @Override
    public int compareTo(final Actions action) {
        return this.id.compareTo(action.getID());
    }

    /**
     * Return the object id of this action
     *
     * @return id
     */
    public ObjectId getID() {
        return this.id;
    }

    /**
     * Return the statement that this action performs
     *
     * @return statement
     */
    public Statement getStatement() {
        return this.statement;
    }

    /**
     * @return the taskID
     */
    public ObjectId getTaskID() {
        return taskID;
    }

    /**
     * @param taskID the taskID to set
     */
    public void setTaskID(ObjectId taskID) {
        this.taskID = taskID;
    }
}
