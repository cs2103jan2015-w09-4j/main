package w094j.ctrl8.pojo;

import java.util.ArrayList;
import java.util.List;

import w094j.ctrl8.data.AliasData;

//@author A0112521B

public class DBfile {

    private List<AliasData> aliasDataList;
    private Config config;
    private History history;
    private List<Task> taskList;

    public DBfile() {
        this.aliasDataList = new ArrayList<AliasData>();
        this.config = new Config();
        this.history = new History();
        this.taskList = new ArrayList<Task>();
    }

    public DBfile(List<AliasData> aliasDataList, Config config,
            History history, List<Task> taskList) {
        this.aliasDataList = aliasDataList;
        this.config = config;
        this.history = history;
        this.taskList = taskList;
    }

    /**
     * @return the aliasDataList
     */
    public List<AliasData> getAliasDataList() {
        return this.aliasDataList;
    }

    /**
     * @return the config
     */
    public Config getConfig() {
        return this.config;
    }

    /**
     * @return the history
     */
    public History getHistory() {
        return this.history;
    }

    /**
     * @return the taskList
     */
    public List<Task> getTaskList() {
        return this.taskList;
    }

    /**
     * @param aliasDataList
     *            the aliasDataList to set
     */
    public void setAliasDataList(List<AliasData> aliasDataList) {
        this.aliasDataList = aliasDataList;
    }

    /**
     * @param config
     *            the config to set
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * @param history
     *            the history to set
     */
    public void setHistory(History history) {
        this.history = history;
    }

    /**
     * @param taskList
     *            the taskList to set
     */
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

}
