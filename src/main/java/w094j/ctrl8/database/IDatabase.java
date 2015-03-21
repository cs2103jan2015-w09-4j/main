package w094j.ctrl8.database;

import java.io.IOException;
import java.util.List;

import w094j.ctrl8.pojo.Config;
import w094j.ctrl8.pojo.Task;

//@author A0112521B

public interface IDatabase {

    /**
     * @param title
     * @return true if the title is in taskList
     */
    @Deprecated
    boolean containsTaskTitle(String title);

    /**
     * Delete the task if found and write the file. It will check its title,
     * category, description, start date, end date, location, priority, reminder
     * and status.
     *
     * @param task
     * @return true if the task is found and deleted
     */
    @Deprecated
    boolean deleteTask(Task task);

    /**
     * Download data from Disk and Google
     *
     * @throws IOException
     */
    void downloadFromStorage() throws IOException;

    /**
     * @return Config
     */
    Config getConfig();

    /**
     * @return List<Task> from the database
     */
    List<Task> getTaskList();

    /**
     * Save and write the file
     */
    @Deprecated
    void save();

    /**
     * Save the new task to the task list and write the file
     *
     * @param newTask
     */
    @Deprecated
    void saveTask(Task newTask);

    /**
     * Store data to Disk and Google
     */
    void saveToStorage();

    /**
     * Sync Data
     */
    void sync();

}
