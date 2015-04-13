//@author A0112521B
package w094j.ctrl8.database;

import java.io.IOException;
import java.security.GeneralSecurityException;

import w094j.ctrl8.data.Data;
import w094j.ctrl8.database.config.Config;
import w094j.ctrl8.exception.DataException;

/**
 * Interface for Database
 */
public interface IDatabase {

    /**
     * Download data from Disk and Google
     *
     * @throws IOException
     * @throws Exception
     */
    void downloadFromStorage() throws IOException, Exception;

    /**
     * @return Config
     */
    Config getConfig();

    /**
     * @return List<Task> from the database
     */
    Data getData();

    /**
     * Store data to Disk and Google
     *
     * @throws DataException
     *             when the task is errornous.
     * @throws IOException
     *             when the connection to Google has issues or when the internet
     *             has issues.
     * @throws GeneralSecurityException
     *             when there is file permission issues.
     */
    void saveToStorage() throws GeneralSecurityException, IOException,
            DataException;

    /**
     * Sync with Google Calendar and Google Task
     *
     * @throws Exception
     */
    void sync() throws Exception;

}
