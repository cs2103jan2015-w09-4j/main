package w094j.ctrl8.pojo;

import w094j.ctrl8.data.Data;
import w094j.ctrl8.database.config.Config;

//@author A0112521B

/**
 * TODO
 */
public class DBfile {

    private Config config;
    private Data data;

    /**
     * TODO
     */
    public DBfile() {
        this.config = new Config();
        this.data = new Data();
    }

    /**
     * TODO
     * 
     * @param data
     * @param config
     */
    public DBfile(Data data, Config config) {
        this.config = config;
        this.data = new Data();
    }

    /**
     * @return the config
     */
    public Config getConfig() {
        return this.config;
    }

    /**
     * @return the data
     */
    public Data getData() {
        return this.data;
    }

    /**
     * @param config
     *            the config to set
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Data data) {
        this.data = data;
    }

}
