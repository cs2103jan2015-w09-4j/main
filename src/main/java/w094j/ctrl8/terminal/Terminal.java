package w094j.ctrl8.terminal;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import w094j.ctrl8.database.Database;
import w094j.ctrl8.display.CLIDisplay;
import w094j.ctrl8.display.Display;
import w094j.ctrl8.exception.CommandExecutionException;
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
    private static final int TASK_MAP_MINIMUM_SIZE = 1; /*
     * a task map should
     * contain at least one
     * entry
     */

    Database database;
    Display display;
    HashMap<String, Task> taskMap;

    public Terminal(Config conf, Display window) {
        this.display = window;
        this.buildTaskMap();
        this.runTerminal();
    }

    public Terminal(Display window) {
        this.display = window;
        this.buildTaskMap();
        this.runTerminal();
    }

    /**
     * TODO: implement a way to dump data from program to an external file for
     * future reference. E.g for next time the program is run.
     */
    public static void pushData() {
        // TODO Auto-generated method stub

    }

    /**
     * Part of CRUD: Add. Throws two types of Exceptions [1:
     * CommandExecutionException] [2. TaskOverwriteException (expected throw
     * from Database)] Refer to Issue #47
     *
     * @param task
     *            The task to add to the database, it should be properly
     *            constructed otherwise Database would run into issues
     */
    public void add(Task task) {
        // Make sure there is at least a proper task title
        assert (task.getTaskTitle() != null);

        try {
            // Add to taskmap?
            this.taskMap.put(task.getTaskTitle(), task);
            // Add to database
            this.database.saveTask(task);

            // Informs user that his add statement is successful
            this.display.outputMessage(task.getTaskTitle()
                    + NormalMessage.ADD_TASK_SUCCESSFUL);

        } catch (Exception e) {
            try {
                throw new CommandExecutionException(e.getMessage());
            } catch (CommandExecutionException e1) {
                // Program should not reach here
            }
        }
    }

    /**
     * Displays an output message requesting for the next user input. This may
     * be empty if the UI does not require such.
     */
    public void displayNextCommandRequest() {
        if (this.display instanceof CLIDisplay) {
            this.display
            .outputMessage(NormalMessage.DISPLAY_NEXT_COMMAND_REQUEST);
        } else {
            // TODO When GUI Display development begins
        }
    }

    /*
     * Function is called when an exit statement is executed. Performs a cleanup
     * before terminating the terminal See Issue #74 on github
     */
    public void exit() {
        this.display.outputMessage(NormalMessage.EXIT_COMMAND);
        this.cleanUp();
    }

    /**
     * Part of CRUD: Display
     *
     * @return an array of all the Task objects, returns null if task map does
     *         not contain any entry
     */
    public Task[] getAllTask() {
        if (this.taskMap.size() < TASK_MAP_MINIMUM_SIZE) {
            /*
             * taskMap size is illegal, most likely cause is that the task map
             * is empty
             */
            return null;
        } else {
            Task[] taskList = new Task[this.taskMap.size()];

            int i = 0;
            for (Map.Entry<String, Task> taskEntry : this.taskMap.entrySet()) {
                // String key = taskEntry.getKey();
                Task task = taskEntry.getValue();

                taskList[i] = task;
                i++;
            }
            return taskList;
        }
    }

    /**
     * Part of CRUD: Delete
     *
     * @param taskID
     * @return true if deleting the task with the given ID was successful,
     *         otherwise returns false
     */
    public boolean taskDelete(String taskID) {
        try {
            /* Check if key exists in taskmap */
            if (this.taskMap.containsKey(taskID)) {
                this.database.delete(this.taskMap.get(taskID));
                this.taskMap.remove(taskID);
                return true;
            } else {
                throw new Exception("TaskID does not exist");
            }
        } catch (Exception e) {
            // TODO: Define a more specific exception
            return false;
        }
    }

    /**
     * Part of CRUD: Update
     *
     * @param task
     * @param newStartDate
     * @param newEndDate
     */
    public void taskUpdate(Task task, Date newStartDate, Date newEndDate) {
        task.setStartDate(newStartDate);
        task.setEndDate(newEndDate);

        this.updateTaskMap(task);
    }

    /**
     * Part of CRUD: Update
     *
     * @param task
     * @param newPriority
     */
    public void taskUpdate(Task task, int newPriority) {
        task.setPriority(newPriority);

        this.updateTaskMap(task);
    }

    /**
     * Part of CRUD: Update
     *
     * @param task
     * @param newTaskName
     */
    public void taskUpdate(Task task, String newTaskName) {
        String oldTaskName = task.getTaskTitle();
        task.setTaskTitle(newTaskName);

        this.updateTaskMap(oldTaskName, task);
    }

    /**
     * Part of CRUD: Update
     *
     * @param task
     * @param newTaskName
     * @param newStartDate
     * @param newEndDate
     * @param newPriority
     */
    public void taskUpdate(Task task, String newTaskName, Date newStartDate,
            Date newEndDate, int newPriority) {
        String oldTaskName = task.getTaskTitle();
        task.setTaskTitle(newTaskName);
        task.setStartDate(newStartDate);
        task.setEndDate(newEndDate);
        task.setPriority(newPriority);

        this.updateTaskMap(oldTaskName, task);
    }

    /**
     * Initialises the taskMap based on what the datastore currently contains
     */
    private void buildTaskMap() {
        /*
         * TODO: Instantiate a database object and get all the tasks from the
         * data base
         */

        Task[] allTasks = this.makeDummyTaskList();/*
         * TODO: currently a
         * placeholder to simulate
         * task adding to tree
         */

        /* Intialise the taskMap with all the tasks that datastore provides */
        this.taskMap = new HashMap<String, Task>();
        for (Task task : allTasks) {
            assert (task != null); /*
                                    * All task objects in allTasks[] should not
                                    * be null
                                    */
            assert (!this.taskMap.containsKey(task.getTaskTitle()));
            /*
             * The taskmap should not already contain a task with the same key
             * Assert fail implies database has overlap
             */

            this.taskMap.put(task.getTaskTitle(), task);
        }
    }

    /**
     * TODO: Any remainder operations left to do after 'exit' command is invoked
     * goes here. This includes dumping information into an external text file.
     * Or (maybe) editing an external config file
     */
    private void cleanUp() {
        // TODO Auto-generated method stub
        pushData();

    }

    /**
     * This is a temporary function to create a dummy task list for testing
     * purposes
     *
     * @return Task[] that contains a series of tasks
     */
    private Task[] makeDummyTaskList() {
        Task[] taskList = new Task[2];
        /* for convenience, all tasks are floating tasks(least parameters) */
        taskList[0] = new Task("Complete CS2103T", 1000, null);
        taskList[1] = new Task("S/U CS2103T", 1, null);

        return taskList;
    }

    /**
     * The Read-Evaluate-Reply-Loop (REPL) of the program. Continues to parse
     * user inputs until 'exit' is invoked
     */
    private void runTerminal() {
        boolean continueExecution = true;
        while (continueExecution) {
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
     * Updates the taskMap, removing an old entry and adding a new entry with
     * the input task object
     *
     * @param oldKey
     * @param task
     */
    private void updateTaskMap(String oldKey, Task task) {
        assert (this.taskMap.containsKey(oldKey));
        assert (this.taskMap.containsKey(task.getTaskTitle()));
        this.taskMap.remove(oldKey);
        this.taskMap.put(task.getTaskTitle(), task);
    }

    /**
     * Updates the taskMap when the task key already exists on the taskMap
     *
     * @param task
     */
    private void updateTaskMap(Task task) {
        assert (this.taskMap.containsKey(task.getTaskTitle()));
        this.taskMap.replace(task.getTaskTitle(), task);

    }
}
