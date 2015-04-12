//@author A0112521B
package w094j.ctrl8.database;

import java.io.IOException;

import w094j.ctrl8.data.Data;
import w094j.ctrl8.database.config.Config;

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
     * @throws Exception
     */
    void saveToStorage() throws Exception;

    /**
     * Sync with Google Calendar and Google Task
     *
     * @throws Exception
     */
    void sync() throws Exception;

}
