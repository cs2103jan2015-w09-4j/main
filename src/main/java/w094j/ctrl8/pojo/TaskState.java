package w094j.ctrl8.pojo;

import java.util.ArrayList;

import w094j.ctrl8.parse.statement.Statement;

public class TaskState {
    private Task initTask;
    private Task finalTask;
    private ArrayList<Actions> actionsList;
    private Actions addAction;
    
    public TaskState(Task task, Statement statement) {
        this.initTask = new Task(task);
        this.finalTask = task;
        this.actionsList = new ArrayList<Actions>();
        this.addAction = new Actions(statement, task.getId());
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
    
    public Actions getAddActions(){
        return this.addAction;
    }

    public boolean remove(Actions actionToBeDel) {
        boolean isHistoryEmpty = false;
        if (actionToBeDel.getID() == this.addAction.getID()){
            this.addAction = null;
        }
        for(int i=0 ; i < this.actionsList.size(); i++){
            if (actionToBeDel.getID() == this.actionsList.get(i).getID()){
                this.actionsList.remove(i);
                break;
            }
        }
        
        if(this.actionsList.isEmpty() && this.addAction == null && this.finalTask == null){
            isHistoryEmpty = true;
        }
        
        return isHistoryEmpty;
    }
}
