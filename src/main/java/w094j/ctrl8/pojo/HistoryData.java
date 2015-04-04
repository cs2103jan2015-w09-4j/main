package w094j.ctrl8.pojo;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.statement.Statement;

//@author A0112092W
/**
 * 
 * This class is for the action's history in an ArrayList
 */
public class HistoryData {
    // The list to show user according to order
    private ArrayList<Statement> ActionList;
    // Store the task's index in taskList according to actionList
    private ArrayList<Integer> taskIndex;
    // All the task used currently
    private ArrayList<String> taskList;
    // The statement list of each task
    private ArrayList<ArrayList<Statement>> taskActionList;
    
    private static Logger logger = LoggerFactory.getLogger(HistoryData.class);
    /**
     * Constructor for History Object
     */
    public HistoryData(){
        this.ActionList = new ArrayList<Statement>();
        this.taskList = new ArrayList<String>();
        this.taskActionList = new ArrayList<ArrayList<Statement>>();
        this.taskIndex = new ArrayList<Integer>();
    }
    
    /**
     * @param history
     */
    public HistoryData(HistoryData history) {

        this.ActionList = new ArrayList<Statement>();
        this.taskIndex = new ArrayList<Integer>();
        this.taskList = new ArrayList<String>();
        this.taskActionList = new ArrayList<ArrayList<Statement>>();

        
        for(int i=0;i<history.getHistorySize();i++){
            this.ActionList.add(history.getHistory(i));
            this.addTask(history.getTaskList().get(i));
            this.taskIndex.add(history.getTaskIndex(i));
            for(int j=0;i<this.taskActionList.size();j++){
                this.taskActionList.get(i).add(history.getTaskActionList().get(i).get(j));
            }
        }
    }

    public ArrayList<ArrayList<Statement>> getTaskActionList() {
        return this.taskActionList;
    }

    private int getTaskIndex(int i) {
        return this.taskIndex.get(i);
    }

    /**
     * return the specific history with an index in the ArrayList
     * @param index
     * @return statement
     */
    public Statement getHistory(int index){
        return this.ActionList.get(index);
    }
    
    /**
     * return the whole history list
     * @return historyList
     */
    public ArrayList<Statement> getHistoryList(){
        return this.ActionList;
    }
    
    /** add a statement history into the history list
     * @param history
     */
    public void addHistory(Statement history){
        this.ActionList.add(history);
    }
    
    /** add a statement history into the history list
     * @param query 
     * @param history
     */
    public void addHistory(String query, Statement history){
        this.ActionList.add(history);
        int index = this.taskList.indexOf(query);
        this.taskIndex.add(index);
        logger.debug("taskActionList size: " + this.taskActionList.size());
        this.taskActionList.get(index).add(history);
    }
    
  
    /**
     * delete the entire history list
     */
    public void deleteAllHistory(){
        this.ActionList.clear();
    }
    
    /**
     * delete the specific history
     * @param index
     * @return statement
     */
    public Statement deleteHistory(int index){
        Statement statement = this.ActionList.remove(index-1);
        int taskIndex = this.taskIndex.remove(index-1);
        this.taskActionList.get(taskIndex).remove(statement);
        return statement;
    }
    
    /**
     * 
     * @return the size of the history
     */
    public int getHistorySize(){
        return this.ActionList.size();
    }
    
    public ArrayList<String> getTaskList(){
        return this.taskList;
    }
    
    public void addTask(String query){
        this.taskList.add(query);
        this.taskActionList.add(new ArrayList<Statement>());
    }
  
    
    public ArrayList<Statement> getTaskAction(String query){
        int index = this.taskList.indexOf(query);
        return this.taskActionList.get(index);
    }
    
    public ArrayList<Integer> getTaskIndex(){
        return this.taskIndex;
    }
}


