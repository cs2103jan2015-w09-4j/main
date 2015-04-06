package w094j.ctrl8.taskmanager;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.data.TaskData;
import w094j.ctrl8.database.Database;
import w094j.ctrl8.database.IDatabase;
import w094j.ctrl8.database.config.TaskManagerConfig;
import w094j.ctrl8.display.Display;
import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.message.CommandExecutionMessage;
import w094j.ctrl8.message.HelpMessage;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.parse.statement.CommandType;
import w094j.ctrl8.parse.statement.Statement;
import w094j.ctrl8.pojo.HistoryData;
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
    private boolean continueExecution = true;
    // Storage object (External)
    private IDatabase database;
    // Interface supporting interaction with user
    private Display display;
    private TaskData taskData;

    /*
     * TODO This function is currently a stub. Until Config object has completed
     * implementation
     */
    /**
     * Constructor for terminal with a config object
     *
     * @param config
     *            Configuration information specifying how Terminal/Display is
     *            to be setup
     */
    public TaskManager(TaskManagerConfig config, AliasData aliasData,
            TaskData taskData) {
        assertNotNull(config); // Should not be a null object
        this.display = Display.getInstance();
        this.aliasData = aliasData;
        /*
         * TODO replace with proper configuration
         */
        try {
            this.database = Database.getInstance();
        } catch (Exception e) {
            Response res = new Response();
            res.reply = e.getMessage();
            this.display.updateUI(res);
        }
        this.taskData = taskData;
    }

    /**
     * Gets the current instance of the TaskManager.
     *
     * @return the current instance.
     */
    public static TaskManager getInstance() {
        if (instance == null) {
            instance = initInstance(new TaskManagerConfig(), new AliasData(),
                    new TaskData());
        }
        return instance;
    }

    /**
     * Creates a Task Manager
     *
     * @return return the Task manager.
     */
    public static TaskManager initInstance(TaskManagerConfig config,
            AliasData aliasData, TaskData taskData) {
        if (instance != null) {
            throw new RuntimeException(
                    "Cannot initialize when it was initialized.");
        } else {
            instance = new TaskManager(config, aliasData, taskData);
        }
        return instance;
    }

    private static void addDoc(IndexWriter w, String title, String description)
            throws IOException {

        // TODO
        // not supposed to be here

        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));

        // use a string field for isbn because we don't want it tokenized
        doc.add(new TextField("description", description == null ? ""
                : description, Field.Store.YES));
        w.addDocument(doc);
    }

    @Override
    public void add(Task task, Statement statement)
            throws CommandExecuteException {
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
            this.taskData.updateTaskMap(task, statement);

        } catch (Exception e) {
            throw new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_UPDATE_TASK_MAP);
        }

        try {
            this.database.saveToStorage();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Informs user that his add statement is successful
        Response res = new Response();
        res.reply = task.getTitle() + NormalMessage.ADD_TASK_SUCCESSFUL;
        this.display.updateUI(res);

    }

    @Override
    public void alias() {
        Response res = new Response();
        logger.debug("in alias taskmanager");
        if (this.aliasData.isEmpty()) {
            res.reply = NormalMessage.ALIAS_MAP_EMPTY;
        } else {
            logger.info("alias is passed to response");
            res.alias = this.aliasData;
        }
        this.display.updateUI(res);

    }

    @Override
    public void aliasAdd(String alias, String value, Statement statement)
            throws CommandExecuteException {
        this.aliasData.addAlias(alias, value);

        Response res = new Response();
        res.reply = alias + NormalMessage.ADD_ALIAS_SUCCESSFUL + value;
        this.display.updateUI(res);
// this.updateHistory(statement);

    }

    @Override
    public void aliasDelete(String query, Statement statement)
            throws DataException {
        String value = this.aliasData.toValue(query);
        AliasData deleted = new AliasData();
        deleted.addAlias(query, value);
        this.aliasData.deleteAlias(query);
        Response res = new Response();
        res.reply = NormalMessage.ALIAS_DELETE_SUCCESSFUL;
        res.alias = deleted;
        this.display.updateUI(res);
// this.updateHistory(statement);
    }

    @Override
    public void delete(String taskID, Statement statement)
            throws CommandExecuteException {
        try {

            logger.debug("boolean of contain key "
                    + this.taskData.containsKey(taskID));

            /* Check if key exists in taskmap */
            if (this.taskData.isTaskExist(taskID)) {
                this.taskData.remove(taskID);

                // Update the database
// this.database.deleteTask(removedTask);
                logger.debug("task removed successfully");

            } else {
                logger.debug("In delete cant find");
                logger.debug("in delete " + this.taskData.numOfTasks());
                throw new CommandExecuteException(
                        CommandExecutionMessage.EXCEPTION_BAD_TASKID);
            }
        } catch (Exception e) {
            throw new CommandExecuteException(e.getMessage());
        }
        // update history
        this.taskData.updateHistory(taskID, statement);

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

    @Override
    public void done(String query, Statement statement)
            throws CommandExecuteException {
        if (this.taskData.isTaskExist(query)) {

            Task task = this.taskData.get(query);
            if (task.getStatus() == true) {
                logger.debug("The task is already done");
            }
            try {
                this.database.saveToStorage();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                // Update the TaskMap
                this.taskData.updateTaskMap(query, task, statement);
            } catch (Exception e) {
                throw new CommandExecuteException(
                        CommandExecutionMessage.EXCEPTION_UPDATE_TASK_MAP);
            }
            // Informs user that his add statement is successful
            Response res = new Response();
            res.reply = task.getTitle() + NormalMessage.DONE_TASK_SUCCESSFUL;
            this.display.updateUI(res);

        }
    }

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

    @Override
    public void help(CommandType command) {
        String helpMessage = this.outputHelpMessage(command);
        Response res = new Response();
        res.reply = helpMessage;
        this.display.updateUI(res);
    }

    @Override
    public void historyClear(int index) {
        Statement statmentRemoved = this.taskData.deleteHistory(index);
        HistoryData temp = new HistoryData();
        temp.addHistory(statmentRemoved);
        Response res = new Response();
        res.reply = NormalMessage.HISTORY_CLEAR_SUCCESSFUL;
        res.history = temp;
        this.display.updateUI(res);

    }

    @Override
    public void historyUndo(int index) throws CommandExecuteException {
        this.taskData.undoHistory(index, this);
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
    public void modify(String query, Task incompleteTask, Statement statement)
            throws CommandExecuteException {

        // check if the task exists
        if (this.taskData.isTaskExist(query)) {
            logger.debug("Modify: the task exist");
            Task task = this.taskData.get(query);

            try {
                // Add to database
// this.database.deleteTask(task);
                task.update(incompleteTask);
// this.database.saveTask(task);
                logger.debug(new Gson().toJson(task));
            } catch (Exception e) {
                logger.debug(e.getMessage());
                throw new CommandExecuteException(e.getMessage());
            }
            try {
                // Update the TaskMap
                this.taskData.updateTaskMap(query, task, statement);
                logger.debug("update task");
            } catch (Exception e) {
                throw new CommandExecuteException(
                        CommandExecutionMessage.EXCEPTION_UPDATE_TASK_MAP);
            }

            // Informs user that his add statement is successful
            Response res = new Response();
            res.reply = task.getTitle() + NormalMessage.MODIFY_TASK_SUCCESSFUL;
            this.display.updateUI(res);
        } else {
            throw new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK);
        }

    }

    @Override
    public void pushData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void search(String query, Task task) {
        try {
            // 0. Specify the analyzer for tokenizing text.
            // The same analyzer should be used for indexing and searching
            StandardAnalyzer analyzer = new StandardAnalyzer();

            // 1. create the index
            Directory index = new RAMDirectory();

            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            IndexWriter w = new IndexWriter(index, config);
            for (Task t : this.taskData.values()) {
                addDoc(w, t.getTitle(), t.getDescription());
            }
            w.close();

            // 2. query
            if (query.length() < 0) {
                throw new Exception("WHY No Query?!");
            }

            // the "title" arg specifies the default field to use
            // when no field is explicitly specified in the query.
            Query q = new QueryParser("title", analyzer).parse(query);

            // 3. search
            int hitsPerPage = 10;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector
                    .create(hitsPerPage);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            // 4. display results
            if (hits.length > 0) {
                try {
                    Task[] taskList = new Task[hits.length];

                    logger.debug("Found:" + hits.length + " hits.");
                    for (int i = 0; i < hits.length; ++i) {
                        int docId = hits[i].doc;

                        Document d = searcher.doc(docId);
                        Task t = this.taskData.get(d.get("title"));

                        taskList[i] = t;
                        logger.debug("Task#" + i + "="
                                + new Gson().toJson(taskList[i]));
                    }

                    Arrays.sort(taskList);

                    Response res = new Response();
                    res.taskList = taskList;
                    logger.debug(new Gson().toJson(res));
                    this.display.updateUI(res);

                } catch (Exception e) {
                    throw new CommandExecuteException(e.getMessage());
                }
            } else {
                logger.debug("No results.");
            }

            // reader can only be closed when there
            // is no need to access the documents any more.
            reader.close();
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }

    @Override
    public void view() throws CommandExecuteException {
        if (this.taskData.numOfTasks() <= TASK_MAP_MINIMUM_SIZE) {
            /*
             * taskMap size is illegal, most likely cause is that the task map
             * is empty
             */
            Response res = new Response();
            res.reply = NormalMessage.NO_TASK_FOUND;
            this.display.updateUI(res);
            logger.debug("no task found" + this.taskData.numOfTasks());
            throw new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK);
        } else {
            try {
                Task[] taskList = new Task[this.taskData.numOfTasks()];
                logger.debug("Number of Tasks:" + this.taskData.values().size());
                int i = 0;
                for (Map.Entry<String, Task> taskEntry : this.taskData
                        .entrySet()) {
                    // String key = taskEntry.getKey();
                    Task task = taskEntry.getValue();

                    taskList[i] = task;
                    logger.debug("Task#" + i + "="
                            + new Gson().toJson(taskList[i]));
                    i++;
                }
                Arrays.sort(taskList);
                Response res = new Response();
                res.taskList = taskList;
                this.display.updateUI(res);

            } catch (Exception e) {
                throw new CommandExecuteException(e.getMessage());
            }
        }
    }

    @Override
    public void viewHistory() throws CommandExecuteException {
        if (this.taskData.getHistory().getHistoryList().size() == 0) {
            /*
             * history is empty
             */
            Response res = new Response();
            res.reply = NormalMessage.NO_HISTORY_FOUND;
            this.display.updateUI(res);

            throw new CommandExecuteException(
                    CommandExecutionMessage.EXCEPTION_MISSING_TASK);
        } else {
            try {

                Response res = new Response();
                res.history = this.taskData.getHistory();
                this.display.updateUI(res);

            } catch (Exception e) {
                throw new CommandExecuteException(e.getMessage());
            }
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
