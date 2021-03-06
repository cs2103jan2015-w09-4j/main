//@author A0112521B
package w094j.ctrl8.database;

import java.io.IOException;

import w094j.ctrl8.database.config.Config;
import w094j.ctrl8.display.Display;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.taskmanager.TaskManager;
import w094j.ctrl8.terminal.Terminal;

/**
 * Factory This class will initialize all instance of Database, Config, Parser,
 * Display, TaskManager and Terminal.
 */
public class Factory {

    private static Factory instance;

    /**
     * @param filePath
     * @throws IOException
     */
    private Factory(String filePath) throws IOException {
        Database db = null;
        try {
            db = Database.initInstance(filePath);
        } catch (Exception e) {
            System.out
            .println("Database corrupted, delete config file and retry.");
        }
        Config config = db.getConfig();

        Parser parser = Parser.initInstance(config.getParser(), db.getData()
                .getAlias());
        Display display = Display.initInstance(config.getDisplay());
        TaskManager taskManager = TaskManager.initInstance(config
                .getTaskManager(), db.getData().getAlias(), db.getData()
                .getTask());
        Terminal.initInstance(config.getTerminal(), taskManager, display,
                parser, db);
    }

    /**
     * Return the instance of this Factory class
     *
     * @return instance
     * @throws IOException
     */
    public static Factory getInstance() throws IOException {
        if (instance == null) {
            throw new RuntimeException(
                    "Cannot initialize when it was initialized.");
        }
        return instance;
    }

    /**
     * Initialize the instance of Factory class and return the instance
     *
     * @param filePath
     * @return instance
     * @throws IOException
     */

    public static Factory initInstance(String filePath) throws IOException {
        if (instance != null) {
            throw new RuntimeException(
                    "Cannot initialize when it was initialized.");
        } else {
            instance = new Factory(filePath);
        }
        return instance;
    }

}
