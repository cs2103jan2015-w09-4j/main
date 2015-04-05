package w094j.ctrl8.parse.statement;


//@author A0065517A
/**
 * Classifies the commands that can be passed in from the user. The list of
 * command accepted are described in this enum:
 *
 * <pre>
 * 1. add
 * 2. alias
 * 3. alias-add
 * 4. alias-delete
 * 5. delete
 * 6. done
 * 7. exit
 * 8. help
 * 9. history
 * 10. history-clear
 * 11. history-undo
 * 12. modify
 * 13. search
 * 14. view
 * </pre>
 */
public enum CommandType {

    /**
     * Adds a task to the task manager. It can add floating task, deadline task,
     * timed task, recurring deadline task or recurring timed task.
     */
    ADD("add"),
    /**
     * List all the aliases in the database.
     */
    ALIAS("alias"),
    /**
     * Adds a new alias to the database.
     */
    ALIAS_ADD("alias-add"),
    /**
     * Deletes a alias from the database.
     */
    ALIAS_DELETE("alias-delete"),
    /**
     * Deletes a task and moves it to the tasks recycle bin.
     */
    DELETE("delete"),
    /**
     * Done command is run when the task is completed.
     */
    DONE("done"),
    /**
     * Exits the task manager.
     */
    EXIT("exit"),
    /**
     * Displays the command syntax and example.
     */
    HELP("help"),
    /**
     * Displays the current history of actions.
     */
    HISTORY("history"),
    /**
     * Clears one, a range or all history.
     */
    HISTORY_CLEAR("history-clear"),
    /**
     * Undo one history action.
     */
    HISTORY_UNDO("history-undo"),
    /**
     * Modifies the task specified, updating the details of the tasks.
     */
    MODIFY("modify"),
    /**
     * Search for a particular task in the task manager.
     */
    SEARCH("search"),
    /**
     * Lists the tasks in the task manager, giving the option to display in
     * different formats.
     */
    VIEW("view");

    private final String commandName;

    private CommandType(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public String toString() {
        return this.commandName;
    }

}
