package w094j.ctrl8.terminal;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import w094j.ctrl8.database.Database;
import w094j.ctrl8.display.Display;
import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.message.ErrorMessage;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.pojo.Config;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.Statement;

//@author A0110787A

/**
 * Class encapsulates an object that acts as a driver for the program. It
 * accepts a config object and an object that implements the Display Interface
 * as arguments. It may interact with one or more Datastore to manage its I/O
 * operations. It parses user inputs and translates them into statements for
 * command objects to perform needed operations (e.g ADD operation)
 */

public class Terminal {
    // Static constants
    private static final String DEFAULT_DATABASE_FILEPATH = "tmp.db";
    private static final int TASK_MAP_MINIMUM_SIZE = 0;

    // Storage object (External)
    Database database;

    Display display;

    // Storage object (Internal)
    HashMap<String, Task> taskMap;

    // Flag that determines whether terminal continues to run or not
    // Default: true
    private boolean continueExecution = true;

    /*
     * TODO This function is currently a stub. Until Config object has completed
     * implementation
     */
    // Constructor for terminal with a config object
    public Terminal(Config conf, Display window) {
        this.display = window;
        try {
            this.database = new Database();
        } catch (Exception e) {
            this.display.outputMessage(e.getMessage());
        }
        this.buildTaskMap();
    }

    // Constructor for a default terminal
    public Terminal(Display window) {
        this.display = window;
        try {
            this.database = new Database();
        } catch (Exception e) {
            this.display.outputMessage(e.getMessage());
        }
        this.buildTaskMap();
    }

    /**
     * Part of CRUD: Add. Throws [CommandExecuteException] Refer to Issue #47
     *
     * @param task
     *            The Task to add to the database, it should be properly
     *            constructed otherwise Database would run into issues
     */
    public void add(Task task) throws CommandExecuteException {
        // Make sure we are not adding an Incomplete task to database
        if (task.getTaskType() == Task.TaskType.INCOMPLETE) {
            throw new CommandExecuteException(
                    ErrorMessage.EXCEPTION_IS_INCOMPLETE_TASK);
        }

        // (deprecated) TaskTitle should not be null
        assert (task.getTitle() != null);

        try {
            // Update Taskmap
            this.updateTaskMap(task);
        } catch (Exception e) {
            throw new CommandExecuteException(
                    ErrorMessage.EXCEPTION_UPDATE_TASK_MAP);
        }
        try {
            // Add to database
            this.database.saveTask(task);

        } catch (Exception e) {
            // Whatever Exception database throws, throw forward
            throw new CommandExecuteException(e.getMessage());
        }

        // Informs user that his add statement is successful
        this.display.outputMessage(task.getTitle()
                + NormalMessage.ADD_TASK_SUCCESSFUL);
    }

    /**
     * Part of CRUD: Delete. Throws [CommandExecuteException]. Refer to Issue
     * #48 Takes in taskID as a String and tests whether it exists on the
     * taskMap. If it does then delete it from the taskMap as well as the
     * Database
     *
     * @param taskID
     */
    public void delete(String taskID) throws CommandExecuteException {
        try {
            /* Check if key exists in taskmap */
            if (this.taskMap.containsKey(taskID)) {
                Task removedTask = this.taskMap.remove(taskID);

                // Update the database
                this.database.deleteTask(removedTask);
            } else {
                throw new CommandExecuteException(
                        ErrorMessage.EXCEPTION_BAD_TASKID);
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
        this.display.outputMessage(NormalMessage.DISPLAY_NEXT_COMMAND_REQUEST);
    }

    /*
     * Function is called when an exit statement is executed. Performs a cleanup
     * before terminating the terminal See Issue #74 on github
     */
    public void exit() {
        this.display.outputMessage(NormalMessage.EXIT_COMMAND);
        this.cleanUp();

        // stop loop
        this.continueExecution = false;
    }

    /**
     * Displays the list of supported syntax. See Issue #80
     */
    public void help() {
        this.display.outputMessage(NormalMessage.HELP_ALL);
    }

    /**
     * Modify the specified Task with new incomplete Task that contains new
     * information Throws [CommandExecutionException] Refer to Issue #50
     *
     * @param query
     * @param incompleteTask
     */
    public void modify(String query, Task incompleteTask)
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
                        ErrorMessage.EXCEPTION_UPDATE_TASK_MAP);
            }

            // Informs user that his add statement is successful
            this.display.outputMessage(task.getTitle()
                    + NormalMessage.MODIFY_TASK_SUCCESSFUL);
        } else {
            throw new CommandExecuteException(
                    ErrorMessage.EXCEPTION_MISSING_TASK);
        }
    }

    /**
     * Instructs database explicitly to save tasks to external file
     */
    public void pushData() {
        this.database.save();

    }

    /**
     * The Read-Evaluate-Reply-Loop (REPL) of the program. Continues to parse
     * user inputs until 'exit' is invoked
     */
    public void runTerminal() {
        this.continueExecution = true;
        while (this.continueExecution) {
            this.displayNextCommandRequest();

            // Passes string to Statement.java to parse into a command
            try {
                Statement.parse(this.display.getUserInput());
            } catch (InvalidParameterException e) {
                this.display.outputMessage(e.getMessage());
            }

        }
    }

    /**
     * View all the task and their information in table format display
     */
    public void view() {
        if (this.taskMap.size() < TASK_MAP_MINIMUM_SIZE) {
            /*
             * taskMap size is illegal, most likely cause is that the task map
             * is empty
             */
            this.display.outputMessage(NormalMessage.NO_TASK_FOUND);
        } else {
            Task[] taskList = new Task[this.taskMap.size()];
            int i = 0;
            for (Map.Entry<String, Task> taskEntry : this.taskMap.entrySet()) {
                // String key = taskEntry.getKey();
                Task task = taskEntry.getValue();

                taskList[i] = task;
                i++;
            }
            this.display.outputTask(taskList);
        }
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
     * Updates the taskMap, removing an old entry and adding a new entry with
     * the input task object
     *
     * @param oldKey
     * @param task
     */
    private void updateTaskMap(String oldKey, Task task) {
        assert (this.taskMap.containsKey(oldKey));
        assert (this.taskMap.containsKey(task.getTitle()));
        this.taskMap.remove(oldKey);
        this.taskMap.put(task.getTitle(), task);
    }

    /**
     * Updates the taskMap when the task key already exists on the taskMap
     *
     * @param task
     */
    private void updateTaskMap(Task task) {
        assert (this.taskMap.containsKey(task.getTitle()));
        this.taskMap.replace(task.getTitle(), task);

    }
}
