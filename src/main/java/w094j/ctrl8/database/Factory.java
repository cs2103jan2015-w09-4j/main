package w094j.ctrl8.database;

import java.io.IOException;

import w094j.ctrl8.database.config.ParserConfig;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.pojo.Config;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

public class Factory {
    private static String DISPLAY = "DISPLAY";
    private static String PARSER = "PARSER";
    private static String TASKMANAGER = "TASKMANAGER";
    private static String TERMINAL = "TERMINAL";

    private Config config = new Config();

    public Factory() {
        this("");
    }

    public Factory(String path) {
        Database database;
        try {
            database = new Database(path);
            this.config = database.getConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param type
     * @return an Object(Display/Parser/TaskManager/Terminal)
     */
    public Object create(String type) {
// if (type.equals(DISPLAY)) {
// return this.buildDisplay(this.config);
// } else if (type.equals(PARSER)) {
// return this.buildParser(this.config);
// } else if (type.equals(TASKMANAGER)) {
// return this.buildTaskManager(this.config);
// } else if (type.equals(TERMINAL)) {
// return this.buildTerminal(this.config);
// }
        return null;
    }

// private Object buildDisplay(Config config) {
// return new Display(config);
// }

    private Parser buildParser(ParserConfig config) {
        return new Parser(config);
    }

// private Object buildTaskManager(Config config) {
// return new TaskManager(config);
// }

    private Object buildTerminal(Config config) {
        return new Terminal(config);
    }

}
