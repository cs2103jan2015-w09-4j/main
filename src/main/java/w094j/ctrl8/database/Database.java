package w094j.ctrl8.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import w094j.ctrl8.data.Data;
import w094j.ctrl8.database.config.Config;
import w094j.ctrl8.pojo.DBfile;
import w094j.ctrl8.statement.Statement;
import w094j.ctrl8.statement.StatementGsonAdaptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class encapsulates all information that is pulled and/or pushed from an
 * external file. Examples include interacting with a local file. Or dumping
 * statement history into an output file. TODO: Cater for Google integration
 */

// @author A0112521B

public class Database implements IDatabase {

    private static final String DEFAULT_FILE_NAME = "database.txt";
    private static Database instance;

    private DBfile file;
    private Path filePath;
    private Gson gson;

    /**
     * @param filePathString
     * @throws IOException
     * @throws NoSuchFileException
     */
    public Database(String filePathString) throws IOException,
            NoSuchFileException {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Statement.class, new StatementGsonAdaptor());
        this.gson = builder.setPrettyPrinting().create();

        if ((filePathString == null) || filePathString.equals("")) {
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
                if (filePathString.substring(filePathString.length() - 1) != File.separator) {
                    filePathString += File.separator;
                    f = new File(filePathString);
                }
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
     * Gets the current instance of the CLIDisplay.
     *
     * @return the current instance.
     * @throws IOException
     * @throws NoSuchFileException
     */
    public static Database getInstance() throws NoSuchFileException,
    IOException {
        if (instance == null) {
            instance = initInstance(null);
        }
        return instance;
    }

    /**
     * @param filePath
     * @return
     * @throws NoSuchFileException
     * @throws IOException
     */
    public static Database initInstance(String filePath)
            throws NoSuchFileException, IOException {

        if (instance != null) {
            throw new RuntimeException(
                    "Cannot initialize when it was initialized.");
        } else {
            instance = new Database(filePath);
        }
        return instance;

    }

    @Override
    public void downloadFromStorage() throws Exception {
        Storage diskStorage = new DiskStorage(this.file, this.filePath,
                this.gson);
        Storage googleCalStorage = new GoogleCalStorage(this.file, this.gson);
        diskStorage.readData();
        googleCalStorage.readData();
    }

    /**
     * @return config
     */
    @Override
    public Config getConfig() {
        return this.file.getConfig();
    }

    @Override
    public Data getData() {
        // TODO Auto-generated method stub
        return this.file.getData();
    }

    // TODO: private DataStore pullFromGoogleCal(GoogleCal googleCalInfo);
    /*
     * TODO: Find out how to interact with Google Calendar API. Determine how to
     * map our variables to their fields and vice versa.
     */
    /*
     * TODO: update return object with relevant parameters
     */

    /**
     * Save and write file.
     *
     * @throws Exception
     */
    @Override
    public void saveToStorage() throws Exception {
        Storage diskStorage = new DiskStorage(this.file, this.filePath,
                this.gson);
        Storage googleCalStorage = new GoogleCalStorage(this.file, this.gson);
        diskStorage.storeData();
        googleCalStorage.storeData();
    }

    @Override
    public void sync() {

    }

    private void readFile() throws IOException {
        String json = new String(Files.readAllBytes(this.filePath));
        this.file = this.gson.fromJson(json, DBfile.class);
    }

    // TODO: I/O options for the datastore
}
