package w094j.ctrl8.pojo;

import java.util.ArrayList;

import w094j.ctrl8.parse.statement.Statement;

public class TaskState {
    private Task initTask;
    private Task finalTask;
    private ArrayList<Actions> actionsList;
    private Actions addAction;
    
    public TaskState(Task task, Statement statement) {
        this.initTask = task;
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

//    public void undoHistory(Actions action, ITaskManager taskManager) throws CommandExecuteException {
//        this.finalTask = this.initTask;
//        int i=0;
//        for(;i<this.actionsList.size();i++){
//           if(action.equals(actionsList.get(i))){
//               break;
//           }
//           actionsList.get(i).getStatement().execute(taskManager, true);
//       }
//        for(int j=i;j<this.actionsList.size();j++)
//        {
//            this.actionsList.remove(j);
//        }
//    }
}
