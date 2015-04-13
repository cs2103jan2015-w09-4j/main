package w094j.ctrl8.taskmanager;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.data.TaskData;
import w094j.ctrl8.database.IDatabase;
import w094j.ctrl8.database.config.TaskManagerConfig;
import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.message.CommandExecutionMessage;
import w094j.ctrl8.message.HelpMessage;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.parse.statement.CommandType;
import w094j.ctrl8.parse.statement.Statement;
import w094j.ctrl8.pojo.Actions;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.pojo.Task;

import com.google.gson.Gson;

//@author A0110787A

/**
 * Class encapsulates an object that acts as a driver for the program. It
 * accepts a config object and an object that implements the Display Interface
 * as arguments. It may interact with one or more Datastore to manage its I/O
 * operations. It parses user inputs and translates them into statements for
 * command objects to perform needed operations (e.g ADD operation)
 */

public class TaskManager implements ITaskManager {

    private static TaskManager instance;

    // Static constants
    private static Logger logger = LoggerFactory.getLogger(TaskManager.class);
    private static final int TASK_MAP_MINIMUM_SIZE = 0;

    private AliasData aliasData;
    // Storage object (External)
    private IDatabase database;
    private TaskData taskData;

    /**
     * Following Singleton pattern, All constructors are made private. Only way
     * to get an instance is through getInstance() and initInstance()
     */
    private TaskManager() {

    }

    /**
     * Constructor for terminal with a config object
     *
     * @param config
     *            Configuration information specifying how Terminal/Display is
     *            to be setup
     * @param aliasData
     * @param taskData
     * @param display
     * @param database
     */
    private TaskManager(TaskManagerConfig config, AliasData aliasData,
            TaskData taskData, IDatabase database) {
        assertNotNull(config); // Should not be a null object
        this.aliasData = aliasData;
        this.database = database;
        this.taskData = taskData;
    }

    /**
     * Gets the current instance of the TaskManager.
     *
     * @return the current instance.
     */
    public static TaskManager getInstance() {
        if (instance == null) {
            throw new RuntimeException(
                    "Task Mananger must be initialized before retrieveing.");
        }
        return instance;
    }

    /**
     * Creates a Task Manager
     *
     * @param config
     * @param aliasData
     * @param taskData
     * @return return the Task manager.
     */
    public static TaskManager initInstance(TaskManagerConfig config,
            AliasData aliasData, TaskData taskData, IDatabase database) {
        if (instance != null) {
            throw new RuntimeException(
                    "Cannot initialize Task Manager as it was initialized before.");
        } else {
            instance = new TaskManager(config, aliasData, taskData, database);
        }
        return instance;
    }

    // @author A0112092W

    /**
     * Basic CRUD: create a Task Creates a task and add into the current
     * database. statement indicates the action that add this task isUndo
     * indicates whether this add is under an undo function
     *
     * @param task
     * @param statement
     * @param isUndo
     */
    @Override
    public Response add(Task task, Statement statement, boolean isUndo) {

        Response res = new Response(statement.getCommand());

        // Task object should not be null
        if (task == null) {
            res.setException(new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_NULL_TASK));
            return res;
        }
        logger.debug("in add task: " + task.getTitle());
        logger.debug("in add " + statement.getCommand() + " "
                + statement.getStatementArgumentsOnly());
        // Make sure we are not adding an Incomplete task to database
        if (task.getTaskType() == Task.TaskType.INCOMPLETE) {
            res.setException(new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_IS_INCOMPLETE_TASK));
            return res;
        }

        boolean isOverlapWarning = false;
        if ((task.getStartDate() != null) && (task.getEndDate() != null)
                && (this.taskData.getTaskList().length > 0)) {

            DateIntervalTree intervalTree = new DateIntervalTree(
                    this.taskData.getTaskList());

            if (intervalTree.isOverlap(task)) {
                isOverlapWarning = true;
            }
        }

        try {
            // Update Taskmap
            this.taskData.updateTaskMap(task, statement, isUndo);
        } catch (Exception e) {
            res.setException(new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_UPDATE_TASK_MAP));
            return res;
        }

        try {
            this.database.saveToStorage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Informs user that his add statement is successful
        if (isUndo == false) {
            res.reply = (isOverlapWarning ? "Warning, there is a clash in timings.\n"
                    : "")
                    + task.getTitle() + NormalMessage.ADD_TASK_SUCCESSFUL;
        }
        return res;
    }

    /**
     * Display all aliases to user
     */
    @Override
    public Response alias(Statement statement) {
        Response res = new Response(statement.getCommand());
        logger.debug("in alias taskmanager");
        if (this.aliasData.isEmpty()) {
            res.reply = NormalMessage.ALIAS_MAP_EMPTY;
        } else {
            logger.info("alias is passed to response");
            res.alias = this.aliasData;
        }
        return res;

    }

    /**
     * Add an alias to current database with the alias string, alias value
     */
    @Override
    public Response aliasAdd(String alias, String value, Statement statement,
            boolean isUndo) {

        Response res = new Response(statement.getCommand());

        // add the alias to aliasData
        this.aliasData.addAlias(alias, value);

        // print add alias to user if this action is not undo
        if (isUndo == false) {
            res.reply = alias + NormalMessage.ADD_ALIAS_SUCCESSFUL + value;
        }

        // save to database
        try {
            this.database.saveToStorage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Delete an alias from current database with the alias string query, alias
     * value
     */
    @Override
    public Response aliasDelete(String query, Statement statement,
            boolean isUndo) {

        Response res = new Response(statement.getCommand());

        try {
            // creates an alias data to be display to user
            String value = this.aliasData.toValue(query);
            AliasData deleted = new AliasData();
            deleted.addAlias(query, value);

            // delete the alias
            this.aliasData.deleteAlias(query);

            // display to user
            if (isUndo == false) {
                res.reply = NormalMessage.ALIAS_DELETE_SUCCESSFUL;
                res.alias = deleted;
            }

            this.database.saveToStorage();
        } catch (Exception e) {
            res.setException(e);
        }
        return res;
    }

    /**
     * Delete an task from current database with a string query
     *
     * @param query
     * @param statement
     * @param isUndo
     */
    @Override
    public Response delete(String query, Statement statement, boolean isUndo) {
        Task task = null;
        Response res = new Response(statement.getCommand());
        try {
            // search the task with the string query
            Task[] taskIdList = this.taskData.search(query);
            if (taskIdList == null) {
                throw new CommandExecuteException(
                        CommandExecutionMessage.EXCEPTION_MISSING_TASK);
            }
            /* Check if key exists in taskStateMap */
            if (taskIdList.length > 0) {
                // only 1 result from searching
                if (taskIdList.length == 1) {
                    task = this.taskData.remove(taskIdList[0].getId(),
                            statement);
                } else {
                    int index = this.chooseIndex(taskIdList,
                            NormalMessage.MODIFIED);
                    task = this.taskData.remove(taskIdList[index].getId(),
                            statement);
                }
                // Update the database
                try {
                    this.database.saveToStorage();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                logger.debug("task removed successfully");

            } else {
                logger.debug("In delete cant find");
                logger.debug("in delete " + this.taskData.numOfTasks());
                throw new CommandExecuteException(
                        CommandExecutionMessage.EXCEPTION_BAD_TASKID);
            }
        } catch (Exception e) {
            res.setException(new CommandExecuteException(e.getMessage()));
            return res;
        }

        // if action is not undo, shows the user delete task succesful
        if (isUndo == false) {
            res.reply = task.getTitle() + NormalMessage.DELETE_TASK_SUCCESSFUL;
        }
        return res;

    }

    /**
     * Done a task by using a search query
     *
     * @param query
     * @param statement
     * @param isUndo
     */
    @Override
    public Response done(String query, Statement statement, boolean isUndo) {

        Response res = new Response(statement.getCommand());

        // search the query by using lucene
        Task[] taskIdList = this.taskData.search(query);
        if (taskIdList == null) {
            res.setException(new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK));
            return res;
        }
        /* Check if key exists in taskStateMap */
        if (taskIdList.length > 0) {
            int index;
            // only one result
            if (taskIdList.length == 1) {
                index = 0;
            } else {
                index = this.chooseIndex(taskIdList, NormalMessage.MODIFIED);
            }

            Task task = taskIdList[index];
            if (task.getStatus() == true) {
                logger.debug("The task is already done");
            }
            task.setStatus(true);

            try {
                // Update the TaskMap
                this.taskData.updateTaskMap(task.getId(), task, statement,
                        isUndo);
            } catch (Exception e) {
                res.setException(new CommandExecuteException(
                        CommandExecutionMessage.EXCEPTION_UPDATE_TASK_MAP));
                return res;
            }
            try {
                this.database.saveToStorage();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // Informs user that his add statement is successful
            if (isUndo == false) {
                res.reply = task.getTitle()
                        + NormalMessage.DONE_TASK_SUCCESSFUL;
            }
        } else {
            res.setException(new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK));
            return res;
        }
        return res;
    }

    /**
     * Exit the program
     */
    @Override
    public Response exit(Statement statement) {
        Response res = new Response(statement.getCommand());
        res.reply = NormalMessage.EXIT_COMMAND;

        try {
            this.database.saveToStorage();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // stop loop
        res.setContinueExecution(false);
        return res;
    }

    /**
     * Display the specified help to user
     *
     * @param command
     */
    @Override
    public Response help(CommandType command, Statement statement) {
        String helpMessage = this.outputHelpMessage(command);
        Response res = new Response(statement.getCommand());
        res.reply = helpMessage;
        return res;
    }

    /**
     * clear a history from the current history list with an index and display
     * it to user
     *
     * @param index
     */
    @Override
    public Response historyClear(int index, Statement statement) {
        // creates an actions that contains the deleted history
        Actions actionsRemoved = this.taskData.deleteHistory(index);
        ArrayList<Actions> temp = new ArrayList<Actions>();
        temp.add(actionsRemoved);

        // display it to user
        Response res = new Response(statement.getCommand());
        res.reply = NormalMessage.HISTORY_CLEAR_SUCCESSFUL;
        res.actions = temp;

        // update the databse
        try {
            this.database.saveToStorage();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    /**
     * undo a history from current history list with an index
     *
     * @param index
     */
    @Override
    public Response historyUndo(int index, Statement statement) {
        Response res = new Response(statement.getCommand());
        // undo the history with index
        try {
            this.taskData.undoHistory(index, this);
        } catch (CommandExecuteException e1) {
            res.setException(e1);
            return res;
        }
        // update the database
        try {
            this.database.saveToStorage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
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
    public Response modify(String query, Task incompleteTask,
            Statement statement, boolean isUndo) {
        Response res = new Response(statement.getCommand());
        // search the task with a query
        Task[] taskIdList = this.taskData.search(query);

        if (taskIdList == null) {
            res.setException(new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK));
            return res;
        }
        /* Check if key exists in taskStateMap */
        if (taskIdList.length > 0) {
            int index;
            if (taskIdList.length == 1) {
                index = 0;
            } else {
                index = this.chooseIndex(taskIdList, NormalMessage.MODIFIED);
            }

            logger.debug("Modify: the task exist");

            // retrieve the task from current data
            Task task = taskIdList[index];

            // modify the task
            try {
                task.update(incompleteTask);
                logger.debug(new Gson().toJson(task));
            } catch (Exception e) {
                logger.debug(e.getMessage());
                res.setException(new CommandExecuteException(e.getMessage()));
                return res;
            }
            try {
                // Update the task data
                this.taskData.updateTaskMap(taskIdList[index].getId(), task,
                        statement, isUndo);
                logger.debug("update task");
                try {
                    this.database.saveToStorage();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (Exception e) {
                res.setException(new CommandExecuteException(
                        CommandExecutionMessage.EXCEPTION_UPDATE_TASK_MAP));
                return res;
            }

            // Informs user that his modify statement is successful
            if (isUndo == false) {
                res.reply = task.getTitle()
                        + NormalMessage.MODIFY_TASK_SUCCESSFUL;
            }
        } else {
            res.setException(new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK));
            return res;
        }
        return res;

    }

    @Override
    public Response search(String query, Statement statement) {
        Response res = new Response(statement.getCommand());
        res.taskList = this.taskData.search(query);
        return res;
    }

    /**
     * display all the tasks to user
     */
    @Override
    public Response view(Statement statement) {

        logger.debug("inside view");
        Response res = new Response(statement.getCommand());
        if (this.taskData.numOfTasks() <= TASK_MAP_MINIMUM_SIZE) {
            /*
             * taskMap size is illegal, most likely cause is that the task map
             * is empty
             */

            res.reply = NormalMessage.NO_TASK_FOUND;
            logger.debug("no task found" + this.taskData.numOfTasks());
            res.setException(new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK));
            return res;
        } else {
            try {
                // get the task list from task data
                Task[] taskList = this.taskData.getTaskList();
                logger.debug("Number of Tasks:" + this.taskData.numOfTasks());

                // display the task to user
                Arrays.sort(taskList);

                res.taskList = taskList;

            } catch (Exception e) {
                res.setException(new CommandExecuteException(e.getMessage()));
                return res;
            }
        }
        return res;
    }

    /**
     * display all history to user according to the action's created time
     */
    @Override
    public Response viewHistory(Statement statement) {
        Response res = new Response(statement.getCommand());
        if (this.taskData.getActionsList().size() == 0) {
            /*
             * history is empty
             */
            res.reply = NormalMessage.NO_HISTORY_FOUND;

            res.setException(new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK));
            return res;
        } else {
            try {
                // display to user
                res.actions = this.taskData.getActionsList();
                return res;
            } catch (Exception e) {
                res.setException(new CommandExecuteException(e.getMessage()));
                return res;
            }
        }

    }

    /**
     * Let the user to choose which task the user wants from search result
     * current return 0 only
     *
     * @param taskIdList
     * @param command
     * @return 0
     */
    private int chooseIndex(Task[] tasks, String command) {

        Arrays.sort(tasks);

        return 0;
    }

    /**
     * @param command
     */
    // @author A0112521B
    private String outputHelpMessage(CommandType command) {
        switch (command) {
            case ADD :
                return this.printTableWithBorder(HelpMessage.ADD_START_INDEX,
                        HelpMessage.ADD_END_INDEX, HelpMessage.TABLE);
            case ALIAS :
                return this.printTableWithBorder(HelpMessage.ALIAS_INDEX,
                        HelpMessage.ALIAS_INDEX, HelpMessage.TABLE);
            case ALIAS_ADD :
                return this.printTableWithBorder(HelpMessage.ALIAS_ADD_INDEX,
                        HelpMessage.ALIAS_ADD_INDEX, HelpMessage.TABLE);
            case ALIAS_DELETE :
                return this.printTableWithBorder(
                        HelpMessage.ALIAS_DELETE_INDEX,
                        HelpMessage.ALIAS_DELETE_INDEX, HelpMessage.TABLE);
            case DELETE :
                return this.printTableWithBorder(HelpMessage.DELETE_INDEX,
                        HelpMessage.DELETE_INDEX, HelpMessage.TABLE);
            case DONE :
                return this.printTableWithBorder(HelpMessage.DONE_INDEX,
                        HelpMessage.DONE_INDEX, HelpMessage.TABLE);
            case EXIT :
                return this.printTableWithBorder(HelpMessage.EXIT_INDEX,
                        HelpMessage.EXIT_INDEX, HelpMessage.TABLE);
            case HELP :
                return this.printTableWithBorder(1, HelpMessage.EXIT_INDEX,
                        HelpMessage.TABLE);
            case HISTORY :
                return this.printTableWithBorder(HelpMessage.HISTORY_INDEX,
                        HelpMessage.HISTORY_INDEX, HelpMessage.TABLE);
            case HISTORY_CLEAR :
                return this.printTableWithBorder(
                        HelpMessage.HISTORY_CLEAR_INDEX,
                        HelpMessage.HISTORY_CLEAR_INDEX, HelpMessage.TABLE);
            case HISTORY_UNDO :
                return this.printTableWithBorder(
                        HelpMessage.HISTORY_UNDO_INDEX,
                        HelpMessage.HISTORY_UNDO_INDEX, HelpMessage.TABLE);
            case MODIFY :
                return this.printTableWithBorder(HelpMessage.MODIFY_INDEX,
                        HelpMessage.MODIFY_INDEX, HelpMessage.TABLE);
            case SEARCH :
                return this.printTableWithBorder(HelpMessage.SEARCH_INDEX,
                        HelpMessage.SEARCH_INDEX, HelpMessage.TABLE);
            case VIEW :
                return this.printTableWithBorder(HelpMessage.VIEW_INDEX,
                        HelpMessage.VIEW_INDEX, HelpMessage.TABLE);
            default :
                assert (false);
        }
        return null;

    }

    /**
     * TODO WHY IS THIS HERE? This method is used to print the table with
     * border. (modified from printTable)
     */
    // @author A0112521B
    private String printTableWithBorder(int startIndex, int endIndex,
            String[][] table) {
        char borderKnot = '+';
        char horizontalBorder = '-';
        char verticalBorder = '|';
        int spaceInfront = 1;
        int spaceBehind = 2;
        char space = ' ';
        String newLine = "\n";
        StringBuilder sb = new StringBuilder();

        // Find out what the maximum number of columns is in any row
        int maxColumns = 0;
        for (String[] element : table) {
            maxColumns = Math.max(element.length, maxColumns);
        }

        // Find the maximum length of a string in each column
        int[] lengths = new int[maxColumns];
        for (int j = 0; j < maxColumns; j++) {
            lengths[j] = Math.max(table[0][j].length(), lengths[j]);
        }
        for (int i = startIndex; i <= endIndex; i++) {
            for (int j = 0; j < maxColumns; j++) {
                lengths[j] = Math.max(table[i][j].length(), lengths[j]);
            }
        }

        for (int j = 0; j < maxColumns; j++) {
            lengths[j] += spaceBehind;
        }

        // Print header
        for (int i = 0; i < maxColumns; i++) {
            sb.append(borderKnot);
            for (int j = 0; j < (lengths[i] + spaceInfront); j++) {
                sb.append(horizontalBorder);
            }
        }
        sb.append(borderKnot);
        sb.append(newLine);
        for (int i = 0; i < maxColumns; i++) {
            sb.append(verticalBorder);
            for (int k = 0; k < spaceInfront; k++) {
                sb.append(space);
            }
            sb.append(table[0][i]);
            for (int j = 0; j < (lengths[i] - table[0][i].length()); j++) {
                sb.append(space);
            }
        }
        sb.append(verticalBorder);
        sb.append(newLine);
        for (int i = 0; i < maxColumns; i++) {

            sb.append(borderKnot);
            for (int j = 0; j < (lengths[i] + spaceInfront); j++) {
                sb.append(horizontalBorder);
            }
        }
        sb.append(borderKnot);
        sb.append(newLine);

        // Print content (from startIndex to endIndex)
        for (int i = startIndex; i <= endIndex; i++) {
            sb.append(verticalBorder);
            for (int j = 0; j < maxColumns; j++) {
                for (int k = 0; k < spaceInfront; k++) {
                    sb.append(space);
                }
                sb.append(table[i][j]);
                for (int k = 0; k < (lengths[j] - table[i][j].length()); k++) {
                    sb.append(space);
                }
                sb.append(verticalBorder);
            }
            sb.append(newLine);

        }
        for (int i = 0; i < maxColumns; i++) {
            sb.append(borderKnot);
            for (int j = 0; j < (lengths[i] + spaceInfront); j++) {
                sb.append(horizontalBorder);
            }
        }
        sb.append(borderKnot);

        return sb.toString();
    }

}
