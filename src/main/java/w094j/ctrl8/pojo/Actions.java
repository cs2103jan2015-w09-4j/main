package w094j.ctrl8.pojo;

import org.bson.types.ObjectId;

import w094j.ctrl8.parse.statement.Statement;

//@author A0112092W
/**
 * This class is the wrapper for the statement that executed and the task that related to it.
 *
 */
public class Actions implements Comparable<Actions> {
    private Statement statement;
    private ObjectId id;
    private String taskID;
    
    /**
     * @param statement
     * @param taskID
     */
    public Actions(Statement statement, String taskID){
        this.statement = statement;
        this.id = new ObjectId();
        this.taskID = taskID;
    }
    
    public Actions(Actions action){
        this.statement = action.getStatement();
        this.id = action.getID();
        this.taskID = action.getTaskID();
    }
    
    public ObjectId getID(){
        return this.id;
    }
    
    public String getTaskID(){
        return this.taskID;
    }
    
    public Statement getStatement(){
        return this.statement;
    }
    
    @Override
    public int compareTo(final Actions action) {
        return this.id.compareTo(action.getID());
    }
}
