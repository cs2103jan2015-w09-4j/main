package w094j.ctrl8.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import w094j.ctrl8.pojo.Config;
import w094j.ctrl8.pojo.DBfile;
import w094j.ctrl8.pojo.Task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class encapsulates all information that is pulled and/or pushed from an
 * external file. Examples include interacting with a local file. Or dumping
 * statement history into an output file. TODO: Cater for Google integration
 */

//@author A0112521B

public class Database {

    private static final String DEFAULT_FILE_NAME = "database.txt";
    private DBfile file;
    private Path filePath;

    /**
     * @throws IOException
     */
    public Database() throws IOException {
        this("");
    }

    /**
     * @param filePathString
     * @throws IOException
     * @throws NoSuchFileException
     */
    public Database(String filePathString) throws IOException,
    NoSuchFileException {

        if (filePathString.equals("")) {
            filePathString = DEFAULT_FILE_NAME;
        }

        File f = new File(filePathString);
        this.file = new DBfile();

        if (f.isFile()) {
            this.filePath = Paths.get(filePathString);
            this.readFile();
        } else if (f.isDirectory()) {
            this.filePath = Paths.get(filePathString + DEFAULT_FILE_NAME);
            this.file = new DBfile();
        } else if (filePathString.endsWith(".txt")) {
            try {
                this.filePath = Paths.get(filePathString);
                if (filePathString.lastIndexOf(File.separator) != -1) {
                    String directory = filePathString.substring(0,
                            filePathString.lastIndexOf(File.separator));
                    f = new File(directory);
                    f.mkdirs();
                }
                this.file = new DBfile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                f.mkdirs();
                new File(filePathString + DEFAULT_FILE_NAME);
                this.filePath = Paths.get(filePathString + DEFAULT_FILE_NAME);
                this.file = new DBfile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * @param title
     * @return true if taskTitle is in taskList
     */
    public boolean containsTaskTitle(String title) {
        for (Task i : this.file.getTaskList()) {
            if (i.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delete a task.
     *
     * @param task
     */
    public void deleteTask(Task task) {
        for (int i = 0; i < this.file.getTaskList().size(); i++) {
            if (this.file.getTaskList().get(i).equals(task)) {
                this.file.getTaskList().remove(i);
                break;
            }
        }
    }

    /**
     * @return config
     */
    public Config getConfig() {
        return this.file.getConfig();
    }

    /**
     * @return taskList
     */
    public List<Task> getTaskList() {
        return this.file.getTaskList();
    }

    public void pushToGoogleCal() {
        /*
         * TODO: Find out how to interact with Google Calendar API. Implement
         * the mappings between our variables to their fields and vice versa.
         */
    }

    /**
     * Save and write file.
     */
    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this.file);
        try {
            Files.write(this.filePath, json.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Save all the tasks.
     *
     * @param newTask
     */
    public void saveTask(Task newTask) {
        this.file.getTaskList().add(newTask);
        this.save();
    }

    // TODO: private DataStore pullFromGoogleCal(GoogleCal googleCalInfo);
    /*
     * TODO: Find out how to interact with Google Calendar API. Determine how to
     * map our variables to their fields and vice versa.
     */
    /*
     * TODO: update return object with relevant parameters
     */

    private void readFile() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = new String(Files.readAllBytes(this.filePath));
        this.file = gson.fromJson(json, DBfile.class);
    }

    // TODO: I/O options for the datastore
}
