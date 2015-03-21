package w094j.ctrl8.pojo;

import java.util.ArrayList;

import w094j.ctrl8.statement.Statement;

//@author A0112092W
/**
 * 
 * This class is for the action's history in an ArrayList
 */
public class History {
    
    private ArrayList<Statement> ActionList;
    
    /**
     * Constructor for History Object
     */
    public History(){
        this.ActionList = new ArrayList<Statement>();
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
    
    /**
     * delete the entire history list
     */
    public void deleteHistory(){
        this.ActionList.clear();
    }
    
    
}
