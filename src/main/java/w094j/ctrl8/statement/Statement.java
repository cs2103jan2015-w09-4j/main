package w094j.ctrl8.statement;

import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.terminal.Terminal;

/**
 * Abstract Class to encapsulate a statement, which composes of its command and
 * its parameter(s). The handling of the parameters is to be handled at its
 * subclasses.
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public abstract class Statement {

    private static Logger logger = LoggerFactory.getLogger(Statement.class);
    private String argumentsString;

    private Command command;

    /**
     * Creates an Statement object with the Command and arguments
     *
     * @param command
     */
    protected Statement(Command command, String statementString) {
        this.command = command;
        this.argumentsString = Command.removeCommandKeyword(statementString);
    }

    /**
     * Takes in a statement string and parses it into a Statement object.
     *
     * @param statementString
     *            a statement string to work on
     * @return the Statement object.
     * @throws InvalidParameterException
     *             when command is not well formed.
     */
    public static Statement parse(String statementString) {
        Command command = Command.parse(statementString);
        Statement statement = null;
        if (command == null) {
            throw new InvalidParameterException(
                    "statement does not contain a valid command.");
        }
        logger.debug("Valid Command, parsed \"" + statementString
                + "\": Command=" + command);
        switch (command) {

            case ADD :
                statement = new AddStatement(statementString);
                break;
            case ALIAS :
                statement = new AliasStatement(statementString);
                break;
            case EXIT :
                statement = new ExitStatement(statementString);
                break;
            case HISTORY :
                statement = new HistoryStatement(statementString);
                break;
            case MODIFY :
                statement = new ModifyStatement(statementString);
                break;
            case VIEW :
                statement = new ViewStatement(statementString);
                break;
            case HELP :
                statement = new HelpStatement(statementString);
                break;
            case DELETE :
                statement = new DeleteStatement(statementString);
                break;
            default :
                // should never reach here
                assert (false);
        }
        return statement;
    }

    /**
     * Checks arguments if it potentially contains some valid argument in it.
     *
     * @param arguments
     * @return <code>true</code> if it contains some arguments, otherwise
     *         <code>false</code>.
     */
    protected static boolean hasParameters(String arguments) {
        return !arguments.trim().equals("");
    }

    /**
     * Executes the command in the specified terminal, performing the specific
     * command related to the respective commands.
     *
     * @param terminal
     *            to execute the command in.
     * @throws CommandExecuteException
     *             when the execution of the command has problems.
     */
    public abstract void execute(Terminal terminal)
            throws CommandExecuteException;

    /**
     * @return the argumentsString
     */
    public String getArgumentsString() {
        return this.argumentsString;
    }

    /**
     * @return the command
     */
    public Command getCommand() {
        return this.command;
    }

    /**
     * @param argumentsString
     *            the argumentsString to set
     */
    public void setArgumentsString(String argumentsString) {
        this.argumentsString = argumentsString;
    }

    /**
     * @param command
     *            the command to set
     */
    public void setCommand(Command command) {
        this.command = command;
    }
}
