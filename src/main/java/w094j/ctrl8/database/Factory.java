package w094j.ctrl8.database;

import java.io.IOException;

import w094j.ctrl8.database.config.Config;
import w094j.ctrl8.display.Display;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.taskmanager.TaskManager;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

public class Factory {

    /**
     * @param filePath
     * @throws IOException
     */
    public Factory(String filePath) throws IOException {

        Database db = Database.initInstance(filePath);

        Config config = db.getConfig();

        Parser.initInstance(config.getParser(), db.getData().getAlias());
        Display.initInstance(config.getDisplay());
        TaskManager.initInstance(config.getTaskManager(), db.getData()
                .getAlias(), db.getData().getTask());
        Terminal.initInstance(config.getTerminal());

    }

}
