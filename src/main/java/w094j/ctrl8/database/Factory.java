package w094j.ctrl8.database;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.database.config.AliasConfig;
import w094j.ctrl8.database.config.DisplayConfig;
import w094j.ctrl8.database.config.ParserConfig;
import w094j.ctrl8.database.config.TaskManagerConfig;
import w094j.ctrl8.display.CLIDisplay;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.taskmanager.TaskManager;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

public class Factory {

    public Factory() {
        AliasData alias = new AliasData();
        AliasConfig aliasConfig = new AliasConfig();
        ParserConfig paserConfig = new ParserConfig(aliasConfig);
        TaskManagerConfig taskManagerConfig = new TaskManagerConfig(aliasConfig);
        this.initDisplay();
        this.initTaskManager(taskManagerConfig);
        this.initParser(paserConfig);
        this.initTerminal();
    }

     /**
     * @param args
     */
    public Factory(String[] args) {
         AliasData alias = new AliasData();
         AliasConfig aliasConfig = new AliasConfig();
         ParserConfig paserConfig = new ParserConfig(aliasConfig);
         DisplayConfig displayConfig = new DisplayConfig();
         TaskManagerConfig taskManagerConfig = new TaskManagerConfig(aliasConfig,displayConfig);
         this.initDisplay();
         this.initTaskManager(taskManagerConfig);
         this.initParser(paserConfig);
         this.initTerminal();
    }

    public CLIDisplay initDisplay() {
         return CLIDisplay.getInstance();
     }
    
     public TaskManager initTaskManager(TaskManagerConfig taskManagerConfig) {
         return TaskManager.getInstance(taskManagerConfig);
     }

    public Parser initParser(ParserConfig paserConfig) {
        return Parser.getInstance(paserConfig);
    }

    public Terminal initTerminal() {
         return Terminal.getInstance();
    }

    public Terminal getTerminal() {
        return Terminal.getInstance();
    }
    
    public Parser getParser(){
        return Parser.getInstance();
    }

}
