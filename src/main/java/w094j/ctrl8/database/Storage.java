package w094j.ctrl8.database;

import java.io.IOException;

import w094j.ctrl8.pojo.DBfile;

//@author A0112521B

/**
 * Storage
 */
public abstract class Storage {
    /**
     * @param file
     */
    public Storage(DBfile file) {
    }

    /**
     * Read Data from Disk and Google
     *
     * @throws IOException
     */
    abstract public void readData() throws IOException;

    /**
     * Store Data to Disk and Google
     *
     * @throws IOException
     * @throws Exception
     */
    abstract public void storeData() throws IOException, Exception;
}
