package w094j.ctrl8.database;

import java.util.List;

import w094j.ctrl8.pojo.Config;
import w094j.ctrl8.pojo.Task;

//@author A0112521B

public interface IDatabase {

    /**
     * @param title
     * @return true if the title is in taskList
     */
    boolean containsTaskTitle(String title);

    /**
     * Delete the task if found and write the file. It will check its title,
     * category, description, start date, end date, location, priority, reminder
     * and status.
     *
     * @param task
     * @return true if the task is found and deleted
     */
    boolean deleteTask(Task task);

    /**
     * @return Config
     */
    Config getConfig();

    /**
     * @return List<Task> from the database
     */
    List<Task> getTaskList();

    /**
     * Push tasks and events to Google Calendar (Not implemented yet)
     */
    void pushToGoogleCal();

    /**
     * Save and write the file
     */
    void save();

    /**
     * Save the new task to the task list and write the file
     *
     * @param newTask
     */
    void saveTask(Task newTask);

}
