package w094j.ctrl8.taskmanager;

import java.io.IOException;
import java.security.GeneralSecurityException;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.parse.statement.CommandType;
import w094j.ctrl8.parse.statement.Statement;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.pojo.Task;

//@author A0110787A
public interface ITaskManager {

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
     * @param statement
     * @throws DataException
     * @throws IOException
     * @throws GeneralSecurityException
     *             @
     */
    public Response add(Task task, Statement statement, boolean isUndo);

    /**
     * View all aliases in the database.
     */
    public Response alias(Statement statement);

    /**
     * adding of alias from user
     *
     * @param alias
     * @param value
     * @param statement
     * @throws DataException
     * @throws IOException
     * @throws GeneralSecurityException
     *             @
     */
    public Response aliasAdd(String alias, String value, Statement statement,
            boolean isUndo);

    /**
     * delete a alias
     *
     * @param query
     * @param statement
     * @throws DataException
     */
    public Response aliasDelete(String query, Statement statement,
            boolean isUndo) throws DataException;

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
     * @param statement
     * @throws CommandExecuteException
     *             @
     */
    public Response delete(String taskID, Statement statement, boolean isUndo)
            throws CommandExecuteException;

    /**
     * Set the task's status to done
     *
     * @param query
     * @param statement
     * @throws CommandExecuteException
     *             @
     */
    public Response done(String query, Statement statement, boolean isUndo)
            throws CommandExecuteException;

    /**
     * Stops the Terminal from continuing its REPL loop. Function is called when
     * an exit statement is executed. Performs a cleanup before terminating the
     * terminal See Issue #74 on github
     */
    public Response exit(Statement statement);

    /**
     * Generates information to assist the user in understanding the available
     * syntax and displays it.
     *
     * @param command
     */
    public Response help(CommandType command, Statement statement); // TODO add
// parameter to support help
// for
// specific
// commands

    /**
     * remove the specified history with index
     *
     * @param index
     */
    public Response historyClear(int parseInt, Statement statement);

    /**
     * undo the action with index in history
     *
     * @param index
     * @throws CommandExecuteException
     * @throws DataException
     *             @
     */
    public Response historyUndo(int parseInt, Statement statement)
            throws CommandExecuteException, DataException;

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
     * @param statement
     * @throws CommandExecuteException
     *             @
     */
    public Response modify(String query, Task incompleteTask,
            Statement statement, boolean isUndo) throws CommandExecuteException;

    /**
     * Saves the Tasks in the task manager,
     */
    public void save();

    /**
     * TODO
     *
     * @param query
     * @param task
     */
    public Response search(String query, Statement statement);

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
    public Response view(Statement statement) throws CommandExecuteException;

    /**
     * view the history of actions
     *
     * @throws CommandExecuteException
     */
    public Response viewHistory(Statement statement)
            throws CommandExecuteException;

}
