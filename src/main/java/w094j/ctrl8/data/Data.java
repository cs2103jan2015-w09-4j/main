package w094j.ctrl8.data;

import w094j.ctrl8.taskmanager.TaskData;

/**
 * TODO
 */
public class Data {

    private AliasData alias;
    private TaskData task;

    /**
     * TODO
     */
    public Data() {
        this.task = new TaskData();
        this.alias = new AliasData();
    }

    /**
     * @return the alias
     */
    public AliasData getAlias() {
        return this.alias;
    }

    /**
     * @return the task
     */
    public TaskData getTask() {
        return this.task;
    }

    /**
     * @param alias
     *            the alias to set
     */
    public void setAlias(AliasData alias) {
        this.alias = alias;
    }

    /**
     * @param task
     *            the task to set
     */
    public void setTask(TaskData task) {
        this.task = task;
    }

}
