package w094j.ctrl8.pojo;

import java.util.ArrayList;

import w094j.ctrl8.parse.statement.Statement;

/**
 *  This class is the data structure that will be use to store the task's state information
 *
 */
//@author A0112092W

public class TaskState {
    // initial state of task. Default : null
    private Task initTask;
    // current/final state of task
    private Task finalTask;
    // the list of action that performs to the task
    private ArrayList<Actions> actionsList;
    
    

    /** Constructor that creates a TaskState with a new task and a statement
     * @param task
     * @param statement
     */
    public TaskState(Task task, Statement statement) {
        this.initTask = null;
        this.finalTask = task;
        this.actionsList = new ArrayList<Actions>();
        if(statement != null){
            this.actionsList.add(new Actions(statement, task.getId()));
        }
    }
    
    
    /** Get the initial state of task 
     * @return initial task
     */
    public Task getInitTask(){
        return this.initTask;
    }
    

    /**  Get the final state of task 
     * 
     * @return final task
     */
    public Task getFinalTask(){
        return this.finalTask;
    }
    
    /** Get the list of action that performs to this task 
     * @return action list
     */
    public ArrayList<Actions> getActionList(){
        return this.actionsList;
    }
    
    /** Set the final task state of this task to task
     * @param task
     */
    public void setFinalTask(Task task){
        this.finalTask = task;
    }
    

    /** Set the initial task state of this task to task
     * @param task
     */
    public void setInitTask(Task task){
        this.initTask = task;
    }
    

    /** Get the action in the action list with its index number in arraylist 
     * @param index
     * @return actions
     */
    public Actions getActions(int index){
        return this.actionsList.get(index);
    }
    

    /** Add an action to current actions list
     * @param action
     */
    public void addActions(Actions action){
        this.actionsList.add(action); 
    }

    /** Remove the action that has the same object id with actionToBeDel in the action list.
     *  Return true if the actions list is empty after the remover.
     * @param actionToBeDel
     * @return isEmpty
     */
    public boolean remove(Actions actionToBeDel) {
        boolean isHistoryEmpty = false;

        for(int i=0 ; i < this.actionsList.size(); i++){
            if (actionToBeDel.getID() == this.actionsList.get(i).getID()){
                this.actionsList.remove(i);
                break;
            }
        }
        
        if(this.actionsList.isEmpty() && this.finalTask == null){
            isHistoryEmpty = true;
        }
        
        return isHistoryEmpty;
    }

    /** Clear the action list
     * 
     */
    public void clearActionList() {

        this.actionsList = new ArrayList<Actions>();
    }
}
