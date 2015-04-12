//@author A0112521B
package w094j.ctrl8.database;

import java.io.IOException;

import w094j.ctrl8.database.config.Config;
import w094j.ctrl8.display.Display;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.taskmanager.TaskManager;
import w094j.ctrl8.terminal.Terminal;

/**
 * Factory
 */
public class Factory {

    private static Factory instance;

    /**
     * @param filePath
     * @throws IOException
     */
    private Factory(String filePath) throws IOException {

        Database db = Database.initInstance(filePath);

        Config config = db.getConfig();

        Parser parser = Parser.initInstance(config.getParser(), db.getData()
                .getAlias());
        Display display = Display.initInstance(config.getDisplay());
        TaskManager taskManager = TaskManager.initInstance(config
                .getTaskManager(), db.getData().getAlias(), db.getData()
                .getTask(), display, db);
        Terminal.initInstance(config.getTerminal(), taskManager, display,
                parser);
    }

    public static Factory getInstance() throws IOException {
        if (instance == null) {
            throw new RuntimeException(
                    "Cannot initialize when it was initialized.");
        }
        return instance;
    }

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
