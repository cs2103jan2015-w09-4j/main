//@author A0112521B
package w094j.ctrl8.database;

import java.io.IOException;

import w094j.ctrl8.exception.DataException;
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
     * @throws DataException
     */
    abstract public void readData() throws IOException, DataException;

    /**
     * Store Data to Disk and Google
     *
     * @throws IOException
     * @throws DataException
     */
    abstract public void storeData() throws IOException, DataException;

    /**
     * Sync with Google
     * 
     * @throws DataException
     * @throws IOException
     */
    abstract public void sync() throws IOException, DataException;
}
