package w094j.ctrl8.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lin Chen-Hsin A0112521B
 */
public class DBfile {

    private Config config;
    private List<Task> taskList;

    public DBfile() {
        this.config = new Config();
        this.taskList = new ArrayList<Task>();
    }

    public DBfile(List<Task> taskList, Config config) {
        this.taskList = taskList;
        this.config = config;
    }

    /**
     * @return the config
     */
    public Config getConfig() {
        return this.config;
    }

    /**
     * @return the taskList
     */
    public List<Task> getTaskList() {
        return this.taskList;
    }

    /**
     * @param config
     *            the config to set
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * @param taskList
     *            the taskList to set
     */
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

}
