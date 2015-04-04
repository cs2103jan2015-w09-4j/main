package w094j.ctrl8.taskmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.pojo.HistoryData;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.Statement;

import com.google.gson.Gson;

public class TaskData {

    private static Logger logger = LoggerFactory.getLogger(TaskData.class);

    // History
    private HistoryData history;
    private HashMap<String, Task> iniTaskMap;

    // Storage object (Internal)
    private HashMap<String, Task> taskMap;

    public TaskData() {
        this.history = new HistoryData();
        this.iniTaskMap = new HashMap<>();
        this.taskMap = new HashMap<>();
    }

    public boolean containsKey(String taskID) {
        // TODO Auto-generated method stub
        return this.taskMap.containsKey(taskID);
    }

    public Statement deleteHistory(int index) {
        return this.history.deleteHistory(index);
    }

    public Set<Entry<String, Task>> entrySet() {
        return this.taskMap.entrySet();
    }

    public Task get(String query) {
        return this.taskMap.get(query);
    }

    /**
     * @return the history
     */
    public HistoryData getHistory() {
        return this.history;
    }

    /**
     * @return the iniTaskMap
     */
    public HashMap<String, Task> getIniTaskMap() {
        return this.iniTaskMap;
    }

    /**
     * @return the taskMap
     */
    public HashMap<String, Task> getTaskMap() {
        return this.taskMap;
    }

    public int numOfTasks() {
        return this.taskMap.size();
    }

    public void remove(String taskID) {
        this.taskMap.remove(taskID);
    }

    /**
     * @param history
     *            the history to set
     */
    public void setHistory(HistoryData history) {
        this.history = history;
    }

    /**
     * @param iniTaskMap
     *            the iniTaskMap to set
     */
    public void setIniTaskMap(HashMap<String, Task> iniTaskMap) {
        this.iniTaskMap = iniTaskMap;
    }

    /**
     * @param taskMap
     *            the taskMap to set
     */
    public void setTaskMap(HashMap<String, Task> taskMap) {
        this.taskMap = taskMap;
    }

    public void undoHistory(int index, ITaskManager taskmanager)
            throws CommandExecuteException {
        index = index - 1;
        if ((index >= this.history.getHistoryList().size()) || (index < 0)) {
            throw new CommandExecuteException(
                    NormalMessage.HISTORY_INDEX_OUT_OF_BOUND);
        } else {
            int taskIndex = this.history.getTaskIndex().get(index);
            Statement statementToBeUndo = this.history.getHistory(index);
            ArrayList<Statement> specificTaskHistory = this.history
                    .getTaskActionList().get(taskIndex);
            int statementIndex = specificTaskHistory.indexOf(statementToBeUndo);
            String query = this.history.getTaskList().get(taskIndex);
            Task task = this.iniTaskMap.get(query);
            if (this.taskMap.get(query) != null) {
                if (task != null) {
                    this.taskMap.replace(query, task);
                } else {
                    this.taskMap.remove(query);
                }
            } else {
                if (task != null) {
                    this.taskMap.put(query, task);
                }
            }

            for (int i = 0; i < statementIndex; i++) {
                Statement statement = specificTaskHistory.get(i);
                statement.execute(taskmanager);
            }

            for (int i = statementIndex; i < specificTaskHistory.size();) {
                specificTaskHistory.remove(i);
            }

            for (int j = index; j < this.history.getHistoryList().size();) {
                if (taskIndex == this.history.getTaskIndex().get(j)) {
                    this.history.getHistoryList().remove(j);
                    this.history.getTaskIndex().remove(j);
                } else {
                    j++;
                }
            }

        }
        logger.debug("end of undo: " + this.history.getTaskIndex().size()
                + index);

    }

    /**
     * update the history of actions
     *
     * @param statement
     *            query
     */
    public void updateHistory(String query, Statement statement) {
        this.history.addHistory(query, statement);

    }

    /**
     * Adds a task to the taskMap as well as removing an older entry. To be used
     * together with modify() command. When taskTitle is modified, its key in
     * the hashmap also changes.
     *
     * @param oldKey
     * @param task
     */
    public void updateTaskMap(String oldKey, Task task, Statement statement) {
        // Check for null params
        assert (oldKey != null);
        assert (task != null);
        // Task should not be incomplete (not a Task delta)
        assert (task.getTaskType() != Task.TaskType.INCOMPLETE);
        // The old key specified should exist
        assert (this.taskMap.containsKey(oldKey));

        this.taskMap.remove(oldKey);
        if (this.taskMap.containsKey(task.getTitle())) {
            this.taskMap.replace(task.getTitle(), task);
            logger.debug("TaskMap: Replace entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        } else {
            logger.debug("TaskMap: Add new entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        }
        // update history
        this.updateHistory(oldKey, statement);
    }

    public Collection<Task> values() {
        return this.taskMap.values();
    }

    /**
     * This is a function to check is a task exist in the task map
     *
     * @param query
     * @return boolean that true shows the task exist in the task map
     */
    boolean isTaskExist(String query) {
        return this.taskMap.containsKey(query);
    }

    /**
     * Adds a task to the taskMap using taskTitle as the key. If key already
     * exists, it overwrites the entry.
     *
     * @param task
     */
    void updateTaskMap(Task task, Statement statement) {

        // Check for null params
        assert (task != null);
        // Task should not be incomplete (not a Task delta)
        assert (task.getTaskType() != Task.TaskType.INCOMPLETE);

        if (this.taskMap.containsKey(task.getTitle())) {
            this.taskMap.replace(task.getTitle(), task);
            logger.debug("TaskMap: Replace entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        } else {
            this.taskMap.put(task.getTitle(), task);
            logger.debug("TaskMap: adding new entry with key "
                    + task.getTitle() + " with " + new Gson().toJson(task));
        }

        // update history
        this.history.addTask(task.getTitle());
        logger.debug("after adding task list: "
                + this.history.getTaskList().size());
        this.updateHistory(task.getTitle(), statement);

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

}
