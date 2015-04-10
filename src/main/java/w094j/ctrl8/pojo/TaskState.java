package w094j.ctrl8.pojo;

import java.util.ArrayList;

import w094j.ctrl8.parse.statement.Statement;

//@author A0112092W
public class TaskState {
    private Task initTask;
    private Task finalTask;
    private ArrayList<Actions> actionsList;
    
    public TaskState(Task task, Statement statement) {
        this.initTask = null;
        this.finalTask = task;
        this.actionsList = new ArrayList<Actions>();
        if(statement != null){
            this.actionsList.add(new Actions(statement, task.getId()));
        }
    }

    public Task getInitTask(){
        return this.initTask;
    }
    
    public Task getFinalTask(){
        return this.finalTask;
    }
    
    public ArrayList<Actions> getActionList(){
        return this.actionsList;
    }
    
    public void setFinalTask(Task task){
        this.finalTask = task;
    }
    
    public void setInitTask(Task task){
        this.initTask = task;
    }
    
    public Actions getActions(int index){
        return this.actionsList.get(index);
    }
    
    public void addActions(Actions action){
        this.actionsList.add(action); 
    }

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

    public void clearActionList() {

        this.actionsList = new ArrayList<Actions>();
    }
}
