package w094j.ctrl8.database;

import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

public class Factory {

    public Factory() {
        //initDisplay();
        //initTaskManager.getInstance();
        this.initParser();
        this.initTerminal();
    }

// private initDisplay() {
// return Display.getInstance();
// }

// private TaskManager initTaskManager() {
// return TaskManager.getInstance();
// }

    private Parser initParser() {
        return Parser.getInstance();
    }

    private Terminal initTerminal() {
        // return Terminal.getInstance();
        return null;
    }

}
