package w094j.ctrl8.data;

//@author A0112521B
/**
 * The Data class contains two types of data which is AliasData and TaskData
 */
public class Data {

    private AliasData alias;
    private TaskData task;

    /**
     * Default constructor of Data
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
