package w094j.ctrl8.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.bson.types.ObjectId;

import w094j.ctrl8.data.Data;
import w094j.ctrl8.database.config.Config;
import w094j.ctrl8.parse.statement.Statement;
import w094j.ctrl8.parse.statement.StatementGsonAdaptor;
import w094j.ctrl8.pojo.DBfile;

import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class encapsulates all information that is pulled and/or pushed from an
 * external file. Examples include interacting with a local file. Or dumping
 * statement history into an output file.
 */

// @author A0112521B

public class Database implements IDatabase {

    private static final String DEFAULT_FILE_NAME = "Ctrl-8.txt";
    private static final String FILE_FORMAT = ".txt";
    private static Database instance;

    private DBfile file;
    private Gson gson;
    private Path path;

    /**
     * @param pathString
     * @throws IOException
     * @throws NoSuchFileException
     */
    public Database(String pathString) throws IOException, NoSuchFileException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Statement.class, new StatementGsonAdaptor());
        builder.registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter());
        this.gson = builder.setPrettyPrinting().create();
        this.path = this.getOrCreatePath(pathString);
        this.file = this.path.toFile().isFile() ? this.getFile() : new DBfile();
    }

    /**
     * Gets the current instance of the CLIDisplay.
     *
     * @return the current instance.
     * @throws IOException
     * @throws NoSuchFileException
     */
    public static Database getInstance() {
        if (instance == null) {
            throw new RuntimeException(
                    "Database must be initialized before retrieveing.");
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
                    "Cannot initialize Database as it was initialized before.");
        } else {
            instance = new Database(filePath);
        }
        return instance;

    }

    @Override
    public void downloadFromStorage() throws Exception {
        Storage diskStorage = new DiskStorage(this.file, this.path, this.gson);
        Storage googleCalStorage = new GoogleStorage(this.file, this.gson);
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
        return this.file.getData();
    }

    /**
     * Save and write file.
     *
     * @throws Exception
     */
    @Override
    public void saveToStorage() throws Exception {
        Storage googleStorage = new GoogleStorage(this.file, this.gson);
        googleStorage.storeData();
        Storage diskStorage = new DiskStorage(this.file, this.path, this.gson);
        diskStorage.storeData();
    }

    @Override
    public void sync() {

    }

    private DBfile getFile() throws IOException {
        String json = new String(Files.readAllBytes(this.path));
        return this.gson.fromJson(json, DBfile.class);
    }

    private Path getOrCreatePath(String directory) throws IOException {
        if ((directory == null) || directory.equals("")) {
            return new File(DEFAULT_FILE_NAME).toPath();
        }

        String filename = DEFAULT_FILE_NAME;
        if (directory.endsWith(FILE_FORMAT)) {
            if (!directory.contains(File.separator)) {
                filename = directory;
                directory = "";
            } else {
                filename = directory.substring(directory
                        .lastIndexOf(File.separator));
                directory = directory.substring(0,
                        directory.lastIndexOf(File.separator));
            }
        }
        new FileDataStoreFactory(new java.io.File(directory));
        return new File(directory + filename).toPath();
    }
}
