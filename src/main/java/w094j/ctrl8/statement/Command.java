package w094j.ctrl8.statement;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * 8. history
 * 9. history-clear
 * 10. history-undo
 * 11. modify
 * 12. search
 * 13. view
 * </pre>
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public enum Command {

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
     * Help command to help the user with the commands and its syntax.
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

    private static Map<String, Command> commandLookup = new ConcurrentHashMap<String, Command>();
    private static Pattern commandPattern;
    private static Logger logger = LoggerFactory.getLogger(Command.class);

    private final String commandName;

    static {
        String delim = "";
        String regex = "^(";
        for (Command eaCommand : EnumSet.allOf(Command.class)) {
            commandLookup.put(eaCommand.toString(), eaCommand);
            regex += delim;
            regex += Pattern.quote(eaCommand.toString());
            delim = "|";
        }
        regex += ")(\\s|$)";
        commandPattern = Pattern.compile(regex);
        logger.info("Command Parser initialized with REGEX:" + regex);
    }

    private Command(String commandName) {
        this.commandName = commandName;
    }

    /**
     * Finds the Enum that corresponds to the command given in the nextLine.
     *
     * @param commandString
     *            command to be matched with the respective Enum.
     * @return Command Enum if found, null otherwise.
     */
    public static Command parse(String commandString) {
        Matcher commandMatcher = commandPattern.matcher(commandString);

        if (commandMatcher.find()) {
            return commandLookup.get(commandMatcher.group().trim()
                    .toLowerCase());
        } else {
            return null;
        }
    }

    /**
     * Removes the command keyword from a statement, if it exist.
     *
     * @param statement
     *            to remove the command keyword.
     * @return Statement without the keyword, leaving only the parameter(s).
     */
    public static String removeCommandKeyword(String statement) {
        Matcher commandMatcher = commandPattern.matcher(statement);
        return commandMatcher.replaceFirst("");
    }

    @Override
    public String toString() {
        return this.commandName;
    }

}
