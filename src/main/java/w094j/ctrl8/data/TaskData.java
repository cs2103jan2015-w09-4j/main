package w094j.ctrl8.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.message.CommandExecutionMessage;
import w094j.ctrl8.parse.statement.Statement;
import w094j.ctrl8.pojo.Actions;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.pojo.TaskState;
import w094j.ctrl8.taskmanager.ITaskManager;

import com.google.gson.Gson;

//@author A0112092W
/**
 * This class contains all the task's data that are needed for task-related
 * operation
 */
public class TaskData {

    private static Logger logger = LoggerFactory.getLogger(TaskData.class);

    // Hash Map that stores every task and its history
    private HashMap<ObjectId, TaskState> taskStateMap;

    /**
     * Default constructor of TaskData
     */
    public TaskData() {
        this.taskStateMap = new HashMap<ObjectId, TaskState>();
    }

    /**
     * add a task for Lucene to search.
     *
     * @param indexWriter
     * @param title
     * @param description
     * @param id
     */
    private static void addDoc(IndexWriter indexWriter, String title,
            String description, ObjectId id) throws IOException {

        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("id", id.toString(), Field.Store.YES));
        // use a string field for isbn because we don't want it tokenized
        doc.add(new TextField("description", description == null ? ""
                : description, Field.Store.YES));
        indexWriter.addDocument(doc);
    }

    /**
     * Check if the taskId exist in the data structure.
     *
     * @param taskID
     * @return true when the data structure contains the taskID.
     */
    public boolean containsKey(String taskID) {
        return this.taskStateMap.containsKey(taskID);
    }

    /**
     * Deletes the History of index.
     *
     * @param index
     * @return the list of actions.
     */
    public Actions deleteHistory(int index) {
        ArrayList<Actions> actions = this.getActionsList();
        Actions actionToBeDel = actions.get(index - 1);

        ObjectId id = actionToBeDel.getTaskID();
        TaskState task = this.taskStateMap.get(id);
        if (task.remove(actionToBeDel)) {
            this.taskStateMap.remove(id);
        }
        return actionToBeDel;
    }

    /**
     * Returns the entry set.
     *
     * @return entrySet.
     */
    public Set<Entry<ObjectId, TaskState>> entrySet() {
        return this.taskStateMap.entrySet();
    }

    /**
     * Actions List of all the Tasks in chronological order.
     *
     * @return the array of actions.
     */
    public ArrayList<Actions> getActionsList() {
        ArrayList<Actions> actions = new ArrayList<Actions>();
        for (TaskState t : this.taskStateMap.values()) {
            for (int i = 0; i < t.getActionList().size(); i++) {
                actions.add(t.getActions(i));
            }
        }
        logger.debug("actionslist size " + actions.size());
        Collections.sort(actions);
        return actions;
    }

    /**
     * Get a particular task.
     *
     * @param taskId
     * @return task.
     */
    public Task getTask(ObjectId taskId) {
        return this.taskStateMap.get(taskId).getFinalTask();
    }

    /**
     * Return the taskList.
     *
     * @return task List
     */
    public Task[] getTaskList() {
        Task[] taskList = new Task[this.numOfTasks()];
        int i = 0;
        for (TaskState t : this.taskStateMap.values()) {
            Task task = t.getFinalTask();
            if (task != null) {
                taskList[i] = task;
                i++;
            }
        }
        Arrays.sort(taskList);
        return taskList;
    }

    /**
     * @return TaskState Map
     */
    public HashMap<ObjectId, TaskState> getTaskStateMap() {
        return this.taskStateMap;
    }

    /**
     * This is a function to check is a task exist in the task map
     *
     * @param query
     * @return boolean that true shows the task exist in the task map
     */
    public boolean isTaskExist(String query) {
        return this.taskStateMap.containsKey(query);
    }

    /**
     * @return number of tasks in the the tasks data structure.
     */
    public int numOfTasks() {
        int size = 0;
        for (TaskState t : this.taskStateMap.values()) {
            if (t.getFinalTask() != null) {
                size++;
            }
        }
        return size;
    }

    /**
     * Removes a task from the data structure,
     *
     * @param id
     *            to use to be removed.
     * @param statement
     *            the statement that executed it.
     * @return the removed task.
     */
    public Task remove(ObjectId id, Statement statement) {
        Task task = this.taskStateMap.get(id).getFinalTask();
        this.taskStateMap.get(id).setFinalTask(null);
        this.taskStateMap.get(id).addActions(new Actions(statement, id));
        return task;
    }

    /**
     * Search all the task that are add into lucene with a query return an array
     * of string that contains all the task's Id. With reference from
     * http://www.lucenetutorial.com/code/HelloLucene.java.
     *
     * @param query
     *            search query.
     * @return the array of tasks.
     * @throws DataException
     *             when there is a problem with the query
     */
    public Task[] search(String query) throws DataException {
        Task[] taskIdList = null;
        try {
            // 0. Specify the analyzer for tokenizing text.
            // The same analyzer should be used for indexing and searching
            Analyzer analyzer = new SimpleAnalyzer();

            Integer parsedInt = null;
            try {
                parsedInt = Integer.parseInt(query);
            } catch (NumberFormatException npe) {
            }
            if (parsedInt != null) {
                analyzer = new StandardAnalyzer();
            }

            // 1. create the index
            Directory index = new RAMDirectory();

            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            IndexWriter w = new IndexWriter(index, config);
            for (ObjectId key : this.getTaskStateMap().keySet()) {
                Task t = this.getTask(key);
                if (t != null) {
                    addDoc(w, t.getTitle(), t.getDescription(), t.getId());
                }
            }
            w.close();

            // 2. query
            if (query.length() < 0) {
                throw new DataException("Query must exist.");
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
                taskIdList = new Task[hits.length];

                logger.debug("Found:" + hits.length + " hits.");
                for (int i = 0; i < hits.length; ++i) {
                    int docId = hits[i].doc;

                    Document d = searcher.doc(docId);
                    ObjectId t = new ObjectId(d.get("id"));

                    taskIdList[i] = this.getTask(t);
                    logger.debug("Task#" + i + "="
                            + new Gson().toJson(taskIdList[i]));
                }
            } else {
                logger.debug("No results.");
            }
            // reader can only be closed when there
            // is no need to access the documents any more.
            reader.close();
        } catch (DataException e) {
            throw e;
        } catch (IOException | ParseException e) {
            throw new DataException(
                    "There was an unexpected error, please report this to our techical team.");
        }
        if (taskIdList != null) {
            Arrays.sort(taskIdList);
        }
        return taskIdList;
    }

    /**
     * @param taskStateMap
     */
    public void setTaskStateMap(HashMap<ObjectId, TaskState> taskStateMap) {
        this.taskStateMap = taskStateMap;
    }

    /**
     * @param index
     * @param taskManager
     * @throws CommandExecuteException
     * @throws DataException
     */
    public void undoHistory(int index, ITaskManager taskManager)
            throws CommandExecuteException, DataException {
        ArrayList<Actions> actions = this.getActionsList();
        if (index > actions.size()) {
            throw new CommandExecuteException(
                    CommandExecutionMessage.INVALID_INDEX);
        }
        Actions action = actions.get(index - 1);
        ObjectId id = action.getTaskID();
        action.getID();
        ArrayList<Actions> taskActions = this.taskStateMap.get(id)
                .getActionList();
        this.taskStateMap.get(id).clearActionList();
        this.taskStateMap.get(id).setFinalTask(
                this.taskStateMap.get(id).getInitTask());
        if (this.taskStateMap.get(id).getInitTask() == null) {
            logger.debug("init task is null");
        }
        int i = 0;
        for (; i < taskActions.size(); i++) {
            if (action.equals(taskActions.get(i))) {
                logger.debug("action same: "
                        + action.equals(taskActions.get(i)));
                break;
            }
            logger.debug("action name: "
                    + taskActions.get(i).getStatement().getCommand()
                    + " "
                    + taskActions.get(i).getStatement()
                    .getStatementArgumentsOnly());
            Statement statement = taskActions.get(i).getStatement();
            statement.execute(taskManager, true);
        }
// for(int j=i;j<taskActions.size();j++)
// {
// task.remove(j);
// }
    }

    /**
     * Adds a task to the taskMap as well as removing an older entry. To be used
     * together with modify() command. When taskTitle is modified, its key in
     * the hashmap also changes.
     *
     * @param id
     * @param task
     * @param statement
     * @param isUndo
     */
    public void updateTaskMap(ObjectId id, Task task, Statement statement,
            Boolean isUndo) {
        // Check for null params
        assert (id != null);
        assert (task != null);
        // Task should not be incomplete (not a Task delta)
        assert (task.getTaskType() != Task.TaskType.INCOMPLETE);

        if (this.taskStateMap.containsKey(id)) {
            if (!(isUndo)) {
                this.taskStateMap.get(id)
                .addActions(new Actions(statement, id));
                logger.debug("isUndo = false");
            }
            this.taskStateMap.get(id).setFinalTask(task);
            logger.debug("TaskMap: Replace entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        } else {
            this.taskStateMap.put(task.getId(), new TaskState(task, statement));
            logger.debug("TaskMap: Add new entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        }
    }

    /**
     * Adds a task to the taskMap using taskTitle as the key. If key already
     * exists, it overwrites the entry.
     *
     * @param task
     * @param statement
     * @param isUndo
     *            is this executing for a undo operation.
     */
    public void updateTaskMap(Task task, Statement statement, boolean isUndo) {

        // Check for null params
        assert (task != null);
        // Task should not be incomplete (not a Task delta)
        assert (task.getTaskType() != Task.TaskType.INCOMPLETE);
        if (this.taskStateMap.containsKey(task.getId())) {
            this.taskStateMap.replace(task.getId(), new TaskState(task,
                    statement));
            this.taskStateMap.get(task.getId()).setFinalTask(task);
            logger.debug("TaskMap: Replace entry with key " + task.getTitle()
                    + " with " + new Gson().toJson(task));
        } else {

            this.taskStateMap.put(task.getId(), new TaskState(task, statement));
            this.taskStateMap.get(task.getId()).setFinalTask(task);
            logger.debug("TaskMap: adding new entry with key "
                    + task.getTitle() + " with " + new Gson().toJson(task));
        }
    }

}
