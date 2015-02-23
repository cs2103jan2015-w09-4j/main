package w094j.ctrl8.terminal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import w094j.ctrl8.database.Database;
import w094j.ctrl8.display.CLIDisplay;
import w094j.ctrl8.display.Display;
import w094j.ctrl8.message.ErrorMessage;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.pojo.Config;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.Statement;

/**
 * Class encapsulates an object that acts as a driver for the program. It
 * accepts a config object and an object that implements the Display Interface
 * as arguments. It may interact with one or more Datastore to manage its I/O
 * operations. It parses user inputs and translates them into statements for
 * command objects to perform needed operations (e.g ADD operation)
 */
/**
 * @author Chen Tze Cheng
 */
/**
 * @author Rodson Chue Le Sheng(A0110787)
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
     * Part of CRUD: Add
     *
     * @param task
     * @return true if adding the task was successful, otherwise returns false
     */
    public boolean taskAdd(Task task) {
        try {
            /* Check if key is already existing in taskMap */
            if (!this.taskMap.containsKey(task)) {
                this.taskMap.put(task.getTaskName(), task);
                this.database.saveTask(task);
                return true;
            } else {
                throw new Exception(ErrorMessage.TASK_KEY_ALREADY_EXISTS);
            }
        } catch (Exception e) {
            // TODO: define a more specific exception
            return false;
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
        String oldTaskName = task.getTaskName();
        task.setTaskName(newTaskName);

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
        String oldTaskName = task.getTaskName();
        task.setTaskName(newTaskName);
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
            assert (!this.taskMap.containsKey(task.getTaskName()));
            /*
             * The taskmap should not already contain a task with the same key
             * Assert fail implies database has overlap
             */

            this.taskMap.put(task.getTaskName(), task);
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
     * Reads input from a stream and translates it into a statement for command
     * object to handle Boolean return value is always true (to continue driver
     * loop) unless 'exit' command is invoked
     *
     * @return true unless 'exit' command is specified indicating end of program
     */
    /**
     * @author Rodson Chue Le Sheng (A0110787)
     */
    private boolean parseInput() {
        String userInput = this.display.getUserInput();
        Statement st;

        try {
            st = Statement.parse(userInput);
        } catch (Exception e) {
            this.display.outputMessage(ErrorMessage.ERROR + e);
            return true;
        }

        try {
            st.execute(this);
        } catch (Exception e) {
            this.display.outputMessage(ErrorMessage.ERROR + e);
            return true;
        }

        switch (st.getCommand()) {
            case EXIT :
                this.display.outputMessage(NormalMessage.EXIT_COMMAND);
                return false;

            case ADD :
                // TODO: call database.save
                return true;

            case DELETE :
                return true;

            case HISTORY :
                return true;
            case LIST :
                return true;
            case MODIFY :
                return true;
            case SEARCH :
                return true;

            default :
                // Should not reach here
                return false;
        }
    }

    /**
     * The Read-Evaluate-Reply-Loop (REPL) of the program. Continues to parse
     * user inputs until 'exit' is invoked
     */
    private void runTerminal() {
        boolean continueExecution = true;
        while (continueExecution) {
            this.displayNextCommandRequest();
            continueExecution = this.parseInput();

        }

        this.cleanUp();

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
        assert (this.taskMap.containsKey(task.getTaskName()));
        this.taskMap.remove(oldKey);
        this.taskMap.put(task.getTaskName(), task);
    }

    /**
     * Updates the taskMap when the task key already exists on the taskMap
     *
     * @param task
     */
    private void updateTaskMap(Task task) {
        assert (this.taskMap.containsKey(task.getTaskName()));
        this.taskMap.replace(task.getTaskName(), task);

    }
}
