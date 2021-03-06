package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

//@author A0112521B
/**
 * Class encapsulates configuration options for program. This includes whether
 * program is running in CLI or GUI mode, where to read/dump data to/from etc.
 */
public class Config implements IStorableElement {

    private DatabaseConfig database;
    private DisplayConfig display;
    private ParserConfig parser;
    private TaskManagerConfig taskManager;
    private TerminalConfig terminal;

    /**
     *
     */
    public Config() {
        this.database = new DatabaseConfig();
        this.display = new DisplayConfig();
        this.parser = new ParserConfig();
        this.taskManager = new TaskManagerConfig();
        this.terminal = new TerminalConfig();
    }

    /**
     * @return the database
     */
    public DatabaseConfig getDatabase() {
        return this.database;
    }

    /**
     * @return the display
     */
    public DisplayConfig getDisplay() {
        return this.display;
    }

    /**
     * @return the parser
     */
    public ParserConfig getParser() {
        return this.parser;
    }

    /**
     * @return the taskManager
     */
    public TaskManagerConfig getTaskManager() {
        return this.taskManager;
    }

    /**
     * @return the terminal
     */
    public TerminalConfig getTerminal() {
        return this.terminal;
    }

    @Override
    public boolean isValid() {
        return this.database.isValid() && this.display.isValid()
                && this.parser.isValid() && this.taskManager.isValid()
                && this.terminal.isValid();
    }

    /**
     * @param database
     *            the database to set
     */
    public void setDatabase(DatabaseConfig database) {
        this.database = database;
    }

    /**
     * @param display
     *            the display to set
     */
    public void setDisplay(DisplayConfig display) {
        this.display = display;
    }

    /**
     * @param parser
     *            the parser to set
     */
    public void setParser(ParserConfig parser) {
        this.parser = parser;
    }

    /**
     * @param taskManager
     *            the taskManager to set
     */
    public void setTaskManager(TaskManagerConfig taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * @param terminal
     *            the terminal to set
     */
    public void setTerminal(TerminalConfig terminal) {
        this.terminal = terminal;
    }

}
