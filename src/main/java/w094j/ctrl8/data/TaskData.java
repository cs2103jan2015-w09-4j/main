package w094j.ctrl8.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
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
    private HashMap<String, String> taskMap;

    public TaskData() {
        this.taskMap = new HashMap<String, String>();
        this.taskStateMap = new HashMap<String, TaskState>();
    }

    
    public boolean containsKey(String taskID) {
        // TODO Auto-generated method stub
        return this.taskMap.containsKey(taskID);
    }


    public Set<Entry<String, String>> entrySet() {
        return this.taskMap.entrySet();
    }

    public Task getTask(String query) {
        
        String id = this.taskMap.get(query);
        return this.taskStateMap.get(id).getFinalTask();
    }
    

    /**
     * @return the taskMap
     */
    public HashMap<String, String> getTaskMap() {
        return this.taskMap;
    }

    /**
     * This is a function to check is a task exist in the task map
     *
     * @param query
     * @return boolean that true shows the task exist in the task map
     */
    public boolean isTaskExist(String query) {
        return this.taskMap.containsKey(query);
    }

    public int numOfTasks() {
        return this.taskMap.size();
    }

    /**
     * @param query
     * @param statement
     */
    public void remove(String query, Statement statement) {
        String id = this.taskMap.remove(query);
        this.taskStateMap.get(id).setFinalTask(null);
        this.taskStateMap.get(id).addActions(new Actions(statement, id));
    }


    /**
     * @param taskMap
     *            the taskMap to set
     */
    public void setTaskMap(HashMap<String, String> taskMap) {
        this.taskMap = taskMap;
    }
    
    /**
     * @param taskStateMap
     */
    public void setTaskStateMap(HashMap<String, TaskState> taskStateMap){
        this.taskStateMap  = taskStateMap;
    }
    

    /**
     * @param index
     * @param taskmanager
     * @throws CommandExecuteException
     */
    public void undoHistory(int index, ITaskManager taskManager)
            throws CommandExecuteException {
        ArrayList<Actions> actions = this.getActionsList();
        Actions action = actions.get(index-1);
        String id = action.getTaskID();
//        this.taskStateMap.get(id).undoHistory(action,taskManager);
        
//        this.taskStateMap.get(id).setFinalTask(this.taskStateMap.get(id).getInitTask());
//        if(this.taskStateMap.get(id).getActions(0).equals(action)){
//            this.taskStateMap.remove(id);
//            this.taskMap.remove(id);
//            return ;
//        }
        this.taskStateMap.get(id).setFinalTask(this.taskStateMap.get(id).getInitTask());
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

    /**
     * Adds a task to the taskMap as well as removing an older entry. To be used
     * together with modify() command. When taskTitle is modified, its key in
     * the hashmap also changes.
     *
     * @param oldKey
     * @param task
     */
    public void updateTaskMap(String oldKey, Task task, Statement statement, Boolean isUndo) {
        // Check for null params
        assert (oldKey != null);
        assert (task != null);
        // Task should not be incomplete (not a Task delta)
        assert (task.getTaskType() != Task.TaskType.INCOMPLETE);
        // The old key specified should exist
        assert (this.taskMap.containsKey(oldKey));

        String id = this.taskMap.remove(oldKey);
        this.taskMap.put(task.getTitle(), id);
        if (this.taskStateMap.containsKey(id)) {
            if (!(isUndo)) {
                this.taskStateMap.get(id).addActions(new Actions(statement,id));
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
        if (this.taskMap.containsKey(task.getTitle())) {
            String id = this.taskMap.get(task.getTitle());
            this.taskStateMap.replace(id, new TaskState(task,statement));
            this.taskStateMap.get(id).setFinalTask(task);
            logger.debug("TaskMap: Replace entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        } else {
            this.taskMap.put(task.getTitle(), task.getId());
            this.taskStateMap.put(task.getId(), new TaskState(task,statement));
            this.taskStateMap.get(task.getId()).setFinalTask(task);
            logger.debug("TaskMap: adding new entry with key "
                    + task.getTitle() + " with " + new Gson().toJson(task));
        }
    }

    public Collection<TaskState> values() {
        return this.taskStateMap.values();
    }

    /**
     * This is a function to check is a task exist in the task map
     *
     * @param task
     * @return boolean that true shows the task exist in the task map
     */
    private boolean isTaskExist(Task task) {
        return this.taskMap.containsKey(task.getTitle());
    }


    public Task[] getTaskList() {
        Task[] taskList = new Task[this.taskMap.size()];
        int i=0;
        for(String t : this.taskMap.values()){
            Task task = this.taskStateMap.get(t).getFinalTask();
            taskList[i] = task;
            i++;
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
        }
        logger.debug("actionslist size " +actions.size());
        Collections.sort(actions);
        return actions; 
    }

}
