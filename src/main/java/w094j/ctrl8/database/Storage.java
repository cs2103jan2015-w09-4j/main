//@author A0112521B
package w094j.ctrl8.database;

import java.io.IOException;

import w094j.ctrl8.pojo.DBfile;

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
     * @throws Exception
     */
    abstract public void readData() throws IOException, Exception;

    /**
     * Store Data to Disk and Google
     *
     * @throws IOException
     * @throws Exception
     */
    abstract public void storeData() throws IOException, Exception;

    /**
     * Sync with Google
     *
     * @throws Exception
     */
    abstract public void sync() throws Exception;
}
