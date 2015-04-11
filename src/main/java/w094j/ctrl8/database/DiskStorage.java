//@author A0112521B
package w094j.ctrl8.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import w094j.ctrl8.pojo.DBfile;

import com.google.gson.Gson;

/**
 * Disk Storage
 */
public class DiskStorage extends Storage {

    DBfile file;
    Path filePath;
    private Gson gson;

    /**
     * @param file
     * @param filePath
     */
    public DiskStorage(DBfile file, Path filePath, Gson gson) {
        super(file);
        this.file = file;
        this.filePath = filePath;
        this.gson = gson;
    }

    @Override
    public void readData() throws IOException {
        String json = new String(Files.readAllBytes(this.filePath));
        this.file = this.gson.fromJson(json, DBfile.class);
    }

    @Override
    public void storeData() {
        String json = this.gson.toJson(this.file);
        try {
            Files.write(this.filePath, json.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
