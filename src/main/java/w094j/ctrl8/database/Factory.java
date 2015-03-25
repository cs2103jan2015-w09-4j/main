package w094j.ctrl8.database;

import w094j.ctrl8.display.IDisplay;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.taskmanager.TaskManager;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

public class Factory {

    public Factory() {
        this.initDisplay();
        this.initTaskManager();
        this.initParser();
        this.initTerminal();
    }

     public IDisplay initDisplay() {
         return IDisplay.getInstance();
     }
    
     public TaskManager initTaskManager() {
         return TaskManager.getInstance();
     }

    public Parser initParser() {
        return Parser.getInstance();
    }

    private Terminal initTerminal() {
         return Terminal.getInstance();
    }

}
