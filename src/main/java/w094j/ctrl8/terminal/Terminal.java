package w094j.ctrl8.terminal;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.database.Database;
import w094j.ctrl8.display.CLIDisplay;
import w094j.ctrl8.display.IDisplay;
import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.message.CommandExecutionMessage;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.pojo.Config;
import w094j.ctrl8.pojo.History;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.CommandType;
import w094j.ctrl8.statement.Statement;

import com.google.gson.Gson;

//@author A0110787A

/**
 * Class encapsulates an object that acts as a driver for the program. It
 * accepts a config object and an object that implements the Display Interface
 * as arguments. It may interact with one or more Datastore to manage its I/O
 * operations. It parses user inputs and translates them into statements for
 * command objects to perform needed operations (e.g ADD operation)
 */

public class Terminal implements ITerminal {

    private static boolean continueExecution = true;
    // Static constants
    private static Logger logger = LoggerFactory.getLogger(Terminal.class);
    private static final int TASK_MAP_MINIMUM_SIZE = 0;

    // Storage object (External)
    Database database;

    // Interface supporting interaction with user
    IDisplay display;

    // Storage object (Internal)
    HashMap<String, Task> taskMap;
    
    //History 
    private History history = new History();
    private AliasData aliasData;

    /**
     * Default Constructor for a terminal with no specifications
     */
    public Terminal() {
        this.display = new CLIDisplay(); // Use CLIDisplay
        try {
            this.database = new Database();
        } catch (Exception e) {
            Response res = new Response();
            res.reply = e.getMessage();
            this.display.updateUI(res);
        }
        this.buildTaskMap();
    }

    /*
     * TODO This function is currently a stub. Until Config object has completed
     * implementation
     */
    /**
     * Constructor for terminal with a config object
     *
     * @param conf
     *            Configuration information specifying how Terminal/Display is
     *            to be setup
     */
    public Terminal(Config conf) {
        assertNotNull(conf); // Should not be a null object

        this.display = new CLIDisplay(); /*
         * TODO replace with proper
         * configuration
         */
        try {
            this.database = new Database();
        } catch (Exception e) {
            Response res = new Response();
            res.reply = e.getMessage();
            this.display.updateUI(res);
        }
        this.buildTaskMap();
    }

    /**
     * @deprecated switch to Terminal(Config)
     *
     *             <pre>
     * Constructor for terminal with a Config object and Display object
     * </pre>
     * @param conf
     * @param display
     */
    @Deprecated
    public Terminal(Config conf, IDisplay display, AliasData aliasData) {
        assertNotNull(conf); // Should not be a null object
        assertNotNull(display); // Should not be a null object

        this.aliasData = aliasData;

        this.display = new CLIDisplay();
        try {
            this.database = new Database();
        } catch (Exception e) {
            Response res = new Response();
            res.reply = e.getMessage();
            this.display.updateUI(res);
        }
        this.buildTaskMap();
    }

    /**
     * Constructor for a terminal with a display provided
     *
     * @param window
     *            Display object specifying how/where messages should be
     *            displayed
     */
    public Terminal(IDisplay window) {
        assertNotNull(window); // Should not be a null object

        this.display = window;
        try {
            this.database = new Database();
        } catch (Exception e) {
            Response res = new Response();
            res.reply = e.getMessage();
            this.display.updateUI(res);
        }
        this.buildTaskMap();
    }

    /**
     * Part of CRUD: Create. Throws [CommandExecuteException] Refer to Issue #47
     *
     * @param task
     *            The Task to add to the database, it should be properly
     *            constructed otherwise Database would run into issues
     */
    @Override
    public void add(Task task,Statement statement) throws CommandExecuteException {
        // Task object should not be null
        if (task == null) {
            throw new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_NULL_TASK);
        }
        // Make sure we are not adding an Incomplete task to database
        if (task.getTaskType() == Task.TaskType.INCOMPLETE) {
            throw new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_IS_INCOMPLETE_TASK);
        }

        try {
            // Update Taskmap
            this.updateTaskMap(task);
        } catch (Exception e) {
            throw new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_UPDATE_TASK_MAP);
        }
        try {
            // Add to database
            this.database.saveTask(task);

        } catch (Exception e) {
            // Whatever Exception database throws, throw forward
            throw new CommandExecuteException(e.getMessage());
        }

        //update history
        updateHistory(statement);
        
        // Informs user that his add statement is successful
        Response res = new Response();
        res.reply = task.getTitle() + NormalMessage.ADD_TASK_SUCCESSFUL;
        this.display.updateUI(res);

        logger.debug("Number of Tasks:" + this.taskMap.values().size());
    }

    /**
     * TODO Test implementation
     * 
     * @param alias
     * @param value
     * @throws CommandExecuteException
     */
    public void aliasAdd(String alias, String value,Statement statement)
            throws CommandExecuteException {
        this.aliasData.addAlias(alias, value);
    }

    /**
     * Part of CRUD: Delete. Throws [CommandExecuteException]. Refer to Issue
     * #48 Takes in taskID as a String and tests whether it exists on the
     * taskMap. If it does then delete it from the taskMap as well as the
     * Database
     *
     * @param taskID
     */
    @Override
    public void delete(String taskID,Statement statement) throws CommandExecuteException {
        try {
            /* Check if key exists in taskmap */
            if (this.taskMap.containsKey(taskID)) {
                Task removedTask = this.taskMap.remove(taskID);

                // Update the database
                this.database.deleteTask(removedTask);
                
               //update history
                updateHistory(statement);
            } else {
                throw new CommandExecuteException(
                        CommandExecutionMessage.EXCEPTION_BAD_TASKID);
            }
        } catch (Exception e) {
            throw new CommandExecuteException(e.getMessage());
        }
    }

    /**
     * Displays an output message requesting for the next user input. This may
     * be empty if the UI does not require such.
     */
    public void displayNextCommandRequest() {
        Response res = new Response();
        res.reply = NormalMessage.DISPLAY_NEXT_COMMAND_REQUEST;
        this.display.updateUI(res);
    }

    /*
     * Function is called when an exit statement is executed. Performs a cleanup
     * before terminating the terminal See Issue #74 on github
     */
    @Override
    public void exit() {
        Response res = new Response();
        res.reply = NormalMessage.EXIT_COMMAND;
        this.display.updateUI(res);

        this.cleanUp();

        // stop loop
        this.continueExecution = false;
    }

    /**
     * This method return the caller a boolean whether this terminal should
     * continue to be executed.
     *
     * @return continueExecution
     */
    public boolean getContinueExecution() {

        return this.continueExecution;
    }

    /**
     * Displays the list of supported syntax. See Issue #80
     */
    @Override
    public void help(CommandType command) {
        Response res = new Response();
        res.command = command;
        this.display.updateUI(res);
    }

    /**
     * Part of CRUD: Update. Modifies the specified Task with new incomplete
     * Task that contains new information Throws [CommandExecutionException]
     * Refer to Issue #50
     *
     * @param query
     * @param incompleteTask
     */
    @Override
    public void modify(String query, Task incompleteTask,Statement statement)
            throws CommandExecuteException {

        // check if the task exists
        if (this.isTaskExist(query)) {

            Task task = this.taskMap.get(query);

            try {
                // Add to database
                this.database.deleteTask(task);
                task.update(incompleteTask);
                this.database.saveTask(task);
            } catch (Exception e) {
                throw new CommandExecuteException(e.getMessage());
            }

            try {
                // Update the TaskMap
                this.updateTaskMap(query, task);
            } catch (Exception e) {
                throw new CommandExecuteException(
                        CommandExecutionMessage.EXCEPTION_UPDATE_TASK_MAP);
            }
            //update history
            updateHistory(statement);
            
            // Informs user that his add statement is successful
            Response res = new Response();
            res.reply = task.getTitle() + NormalMessage.MODIFY_TASK_SUCCESSFUL;
            this.display.updateUI(res);
        } else {
            throw new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK);
        }
        
    }

    /**
     * Instructs database explicitly to save tasks to external file
     */
    @Override
    public void pushData() {
        this.database.save();
    }

    public void search(String query, Task task) {
        // TODO task is the temporary filter, we need to come up with a specific
        // filter object
    }

    /**
     * Part of CRUD: Display. View all the task and their information in table
     * format display
     */
    @Override
    public void view() throws CommandExecuteException {
        if (this.taskMap.size() < TASK_MAP_MINIMUM_SIZE) {
            /*
             * taskMap size is illegal, most likely cause is that the task map
             * is empty
             */
            Response res = new Response();
            res.reply = NormalMessage.NO_TASK_FOUND;
            this.display.updateUI(res);

            throw new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK);
        } else {
            try {
                Task[] taskList = new Task[this.taskMap.size()];
                logger.debug("Number of Tasks:" + this.taskMap.values().size());
                int i = 0;
                for (Map.Entry<String, Task> taskEntry : this.taskMap
                        .entrySet()) {
                    // String key = taskEntry.getKey();
                    Task task = taskEntry.getValue();

                    taskList[i] = task;
                    logger.debug("Task#" + i + "="
                            + new Gson().toJson(taskList[i]));
                    i++;
                }

                Response res = new Response();
                res.taskList = taskList;
                this.display.updateUI(res);

            } catch (Exception e) {
                throw new CommandExecuteException(e.getMessage());
            }
        }
    }
    
    
    public void done(String query,Statement statement) throws CommandExecuteException{
        if (this.isTaskExist(query)) {

            Task task = this.taskMap.get(query);
            if(task.getStatus() == true){
                logger.debug("The task is already done");
            }
            try {
                // Add to database
                this.database.deleteTask(task);
                task.setStatus(true);
                this.database.saveTask(task);
            } catch (Exception e) {
                throw new CommandExecuteException(e.getMessage());
            }

            try {
                // Update the TaskMap
                this.updateTaskMap(query, task);
            } catch (Exception e) {
                throw new CommandExecuteException(
                        CommandExecutionMessage.EXCEPTION_UPDATE_TASK_MAP);
            }
            //update history
            updateHistory(statement);
        }
    }
        
    /**
     * update the history of actions 
     * @param statement
     */
    private void updateHistory(Statement statement){
        this.history.addHistory(statement);
    }

    /**
     * Initializes the taskMap based on what the datastore currently contains
     */
    private void buildTaskMap() {
        assert (this.database != null);
        /*
         * The database should already be instantiated
         */

        List<Task> allTasks = this.database.getTaskList();

        /* Intialise the taskMap with all the tasks that datastore provides */
        this.taskMap = new HashMap<String, Task>();
        for (Task task : allTasks) {
            assert (task != null);
            /*
             * All task objects in allTasks should not be null
             */
            assert (!this.taskMap.containsKey(task.getTitle()));
            /*
             * The taskmap should not already contain a task with the same key
             * Assert fail implies database has problems
             */

            this.taskMap.put(task.getTitle(), task);
        }
    }

    /**
     * TODO: Any remainder operations left to do after 'exit' command is invoked
     * goes here. This includes dumping information into an external text file.
     * Or (maybe) editing an external config file
     */
    private void cleanUp() {
        this.pushData();
    }

    /**
     * This is a function to check is a task exist in the task map
     *
     * @param query
     * @return boolean that true shows the task exist in the task map
     */
    private boolean isTaskExist(String query) {
        return this.taskMap.containsKey(query);
    }

    /**
     * This is a function to check is a task exist in the task map
     *
     * @param task
     * @return boolean that true shows the task exist in the task map
     */
    private boolean isTaskExist(Task task) {
        return this.taskMap.containsKey(task.getTitle());
    }

    /**
     * Adds a task to the taskMap as well as removing an older entry. To be used
     * together with modify() command. When taskTitle is modified, its key in
     * the hashmap also changes.
     *
     * @param oldKey
     * @param task
     */
    private void updateTaskMap(String oldKey, Task task) {
        // Check for null params
        assert (oldKey != null);
        assert (task != null);
        // Task should not be incomplete (not a Task delta)
        assert (task.getTaskType() != Task.TaskType.INCOMPLETE);
        // The old key specified should exist
        assert (this.taskMap.containsKey(oldKey));

        this.taskMap.remove(oldKey);
        if (this.taskMap.containsKey(task.getTitle())) {
            this.taskMap.replace(task.getTitle(), task);
            logger.debug("TaskMap: Replace entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        } else {
            logger.debug("TaskMap: Add new entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        }
    }

    /**
     * Adds a task to the taskMap using taskTitle as the key. If key already
     * exists, it overwrites the entry.
     *
     * @param task
     */
    private void updateTaskMap(Task task) {
        // Check for null params
        assert (task != null);
        // Task should not be incomplete (not a Task delta)
        assert (task.getTaskType() != Task.TaskType.INCOMPLETE);

        if (this.taskMap.containsKey(task.getTitle())) {
            this.taskMap.replace(task.getTitle(), task);
            logger.debug("TaskMap: Replace entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        } else {
            this.taskMap.put(task.getTitle(), task);
            logger.debug("TaskMap: Replace entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        }

    }
}
