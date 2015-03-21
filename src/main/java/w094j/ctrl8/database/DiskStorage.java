package w094j.ctrl8.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import w094j.ctrl8.pojo.DBfile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//@author A0112521B

/**
 * Disk Storage
 */
public class DiskStorage extends Storage {
    DBfile file;
    Path filePath;

    /**
     * @param file
     * @param filePath
     */
    public DiskStorage(DBfile file, Path filePath) {
        super(file);
        this.file = file;
        this.filePath = filePath;
    }

    @Override
    public void readData() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = new String(Files.readAllBytes(this.filePath));
        this.file = gson.fromJson(json, DBfile.class);
    }

    @Override
    public void storeData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this.file);
        try {
            Files.write(this.filePath, json.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
