package w094j.ctrl8.terminal;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.pojo.Task;

public interface ITerminal {

    /**
     * <pre>
     * Part of CRUD: Create
     * </pre>
     *
     * Adds a task to data store. When poorly defined Task is used or execution
     * mischap occurs, CommandExecuteException is thrown
     *
     * @param task
     *            Task object. Expects a well defined Task, otherwise throws
     *            CommandExecuteException
     * @throws CommandExecuteException
     */
    public void add(Task task) throws CommandExecuteException;

    /**
     * <pre>
     * Part of CRUD: Delete
     * </pre>
     *
     * Deletes a task in the data store given the taskID that the object
     * contains. If the taskID does not currently exist in the data store, or
     * any failure in storage operation occurs, CommandExecuteException is
     * thrown.
     *
     * @param taskID
     *            key that represents the task object
     * @throws CommandExecuteException
     */
    public void delete(String taskID) throws CommandExecuteException;

    /**
     * Stops the Terminal from continuing its REPL loop
     */
    public void exit();

    /**
     * Generates information to assist the user in understanding the available
     * syntax and displays it.
     */
    public void help(); // TODO add parameter to support help for specific
// commands

    /**
     * <pre>
     * Part of CRUD: Update
     * </pre>
     *
     * Modifies a task currently existing in the data store with new
     * information, using a query. A matching task is found using the query,
     * then the modify operation is performed. Existing fields are overwritten.
     * Bad query, poorly formed task, or failure in modification operation will
     * throw CommandExecuteException
     *
     * @param query
     *            String that contains what the user wants to search the data
     *            store with.
     * @param incompleteTask
     *            a Task that may not neccessarily have all its fields
     *            completed. Is used as an overwrite.
     * @throws CommandExecuteException
     */
    public void modify(String query, Task incompleteTask)
            throws CommandExecuteException;

    /**
     * Instructs the data store to dump its information into an external file.
     * This saves an instance of the list of Tasks that the Terminal has stored
     * so that data can be recovered in the event of unexpected program
     * termination.
     */
    public void pushData();

    /**
     * Begins the REPL process of the terminal.
     */
    public void runTerminal();

    /**
     * <pre>
     * Part of CRUD: Read
     * </pre>
     *
     * Displays to the user, the list of Tasks that are in the data store. May
     * be an incomplete list if there are too many tasks.
     *
     * @throws CommandExecuteException
     */
    public void view() throws CommandExecuteException;

}
