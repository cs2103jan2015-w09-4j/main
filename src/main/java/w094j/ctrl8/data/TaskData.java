package w094j.ctrl8.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.message.CommandExecutionMessage;
import w094j.ctrl8.parse.statement.Statement;
import w094j.ctrl8.pojo.Actions;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.pojo.TaskState;
import w094j.ctrl8.taskmanager.ITaskManager;

import com.google.gson.Gson;
//@ author A0112092W
public class TaskData {

    private static Logger logger = LoggerFactory.getLogger(TaskData.class);
    
    // Hash Map that stores every task and its history
    private HashMap<String, TaskState> taskStateMap;
    // Hash Map that stores the objectID of every task by their task name
//    private HashMap<String, String> taskMap;

    public TaskData() {
//        this.taskMap = new HashMap<String, String>();
        this.taskStateMap = new HashMap<String, TaskState>();
    }

    
    public boolean containsKey(String taskID) {
        // TODO Auto-generated method stub
        return this.taskStateMap.containsKey(taskID);
    }


    public Set<Entry<String, TaskState>> entrySet() {
        return this.taskStateMap.entrySet();
    }

    public Task getTask(String taskId) {
        return this.taskStateMap.get(taskId).getFinalTask();
    }
    

//    /**
//     * @return the taskMap
//     */
//    public HashMap<String, String> getTaskMap() {
//        return this.taskMap;
//    }

    /**
     * This is a function to check is a task exist in the task map
     *
     * @param query
     * @return boolean that true shows the task exist in the task map
     */
    public boolean isTaskExist(String query) {
        return this.taskStateMap.containsKey(query);
    }

    public int numOfTasks() {
        int size = 0;
        for(TaskState t : this.taskStateMap.values()){
            if(t.getFinalTask()!=null){
                size++;
            }
        }
        return size;
    }

    /**
     * @param query
     * @param statement
     * @return 
     */
    public Task remove(String id, Statement statement) {
        Task task = this.taskStateMap.get(id).getFinalTask();
        this.taskStateMap.get(id).setFinalTask(null);
        this.taskStateMap.get(id).addActions(new Actions(statement, id));
        return task;
    }


//    /**
//     * @param taskMap
//     *            the taskMap to set
//     */
//    public void setTaskMap(HashMap<String, String> taskMap) {
//        this.taskMap = taskMap;
//    }
    
    /**
     * @param taskStateMap
     */
    public void setTaskStateMap(HashMap<String, TaskState> taskStateMap){
        this.taskStateMap  = taskStateMap;
    }
    

    /**
     * @param index
     * @param taskManager 
     * @throws CommandExecuteException
     */
    public void undoHistory(int index, ITaskManager taskManager)
            throws CommandExecuteException {
        ArrayList<Actions> actions = this.getActionsList();
        if(index > actions.size()){
            throw new CommandExecuteException(CommandExecutionMessage.INVALID_INDEX);
        }
        Actions action = actions.get(index-1);
        String id = action.getTaskID();
        ObjectId objId = action.getID();
        logger.debug("task to be undo : " + this.taskStateMap.get(id).getInitTask().getTitle());
        logger.debug(action.getStatement().getCommand().toString());
        logger.debug(this.taskStateMap.get(id).getAddActions().getStatement().getCommand().toString());
        if(objId.equals(this.taskStateMap.get(id).getAddActions().getID())){
            logger.debug("actions is add, whole task deleted");
            this.taskStateMap.remove(id);
        }
        else{
            this.taskStateMap.get(id).setFinalTask(this.taskStateMap.get(id).getInitTask());
            logger.debug(this.taskStateMap.get(id).getInitTask().getTitle());
            logger.debug(this.taskStateMap.get(id).getFinalTask().getTitle());
            int i=0;
            for(;i<this.taskStateMap.get(id).getActionList().size();i++){
               if(action.equals(this.taskStateMap.get(id).getActionList().get(i))){
                   logger.debug("action same: " + action.equals(this.taskStateMap.get(id).getActionList().get(i)));
                   break;
               }
               this.taskStateMap.get(id).getActionList().get(i).getStatement().execute(taskManager, true);
           }
            for(int j=i;j<this.taskStateMap.get(id).getActionList().size();j++)
            {
                this.taskStateMap.get(id).getActionList().remove(j);
            }
        }
    }

    /**
     * Adds a task to the taskMap as well as removing an older entry. To be used
     * together with modify() command. When taskTitle is modified, its key in
     * the hashmap also changes.
     * @param id 
     *
     * @param oldKey
     * @param task
     * @param statement 
     * @param isUndo 
     */
    public void updateTaskMap(String id, Task task, Statement statement, Boolean isUndo) {
        // Check for null params
        assert (id != null);
        assert (task != null);
        // Task should not be incomplete (not a Task delta)
        assert (task.getTaskType() != Task.TaskType.INCOMPLETE);
        
        if (this.taskStateMap.containsKey(id)) {
            if (!(isUndo)) {
                this.taskStateMap.get(id).addActions(new Actions(statement,id));
                logger.debug("isUndo = false");
            }
            this.taskStateMap.get(id).setFinalTask(task);
            logger.debug("TaskMap: Replace entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        } else {
            this.taskStateMap.put(task.getId(), new TaskState(task,statement));
            logger.debug("TaskMap: Add new entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        }
    }

    /**
     * Adds a task to the taskMap using taskTitle as the key. If key already
     * exists, it overwrites the entry.
     *
     * @param task
     * @param statement 
     */
    public void updateTaskMap(Task task, Statement statement) {

        // Check for null params
        assert (task != null);
        // Task should not be incomplete (not a Task delta)
        assert (task.getTaskType() != Task.TaskType.INCOMPLETE);
        if (this.taskStateMap.containsKey(task.getId())) {
            this.taskStateMap.replace(task.getId(), new TaskState(task,statement));
            this.taskStateMap.get(task.getId()).setFinalTask(task);
            logger.debug("TaskMap: Replace entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        } else {
            this.taskStateMap.put(task.getId(), new TaskState(task,statement));
            this.taskStateMap.get(task.getId()).setFinalTask(task);
            logger.debug("TaskMap: adding new entry with key "
                    + task.getTitle() + " with " + new Gson().toJson(task));
        }
    }

//    public Collection<TaskState> values() {
//        return this.taskStateMap.values();
//    }

    /**
     * This is a function to check is a task exist in the task map
     *
     * @param task
     * @return boolean that true shows the task exist in the task map
     */
    private boolean isTaskExist(Task task) {
        return this.taskStateMap.containsKey(task.getTitle());
    }


    public Task[] getTaskList() {
        Task[] taskList = new Task[numOfTasks()];
        int i=0;
        for(TaskState t : this.taskStateMap.values()){
            Task task = t.getFinalTask();
            if (task != null){
                taskList[i] = task;
                i++;
            }
        }
        Arrays.sort(taskList);
        return taskList;
    }


    public ArrayList<Actions> getActionsList() {
        ArrayList<Actions> actions = new ArrayList<Actions>();
        for(TaskState t : this.taskStateMap.values()){
            for(int i=0; i< t.getActionList().size();i++){
                actions.add(t.getActions(i));
            }
            actions.add(t.getAddActions());
        }
        logger.debug("actionslist size " +actions.size());
        Collections.sort(actions);
        return actions; 
    }


    public  HashMap<String, TaskState> getTaskStateMap() {
        return this.taskStateMap;
    }


    public Actions deleteHistory(int index) {
        ArrayList<Actions> actions = this.getActionsList();
        Actions actionToBeDel = actions.get(index-1);
        
        String id = actionToBeDel.getTaskID();
        TaskState task = this.taskStateMap.get(id);
        if(task.remove(actionToBeDel)){
            this.taskStateMap.remove(id);
        }
        return actionToBeDel;
    }
}
