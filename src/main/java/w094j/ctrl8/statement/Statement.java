package w094j.ctrl8.statement;

import java.security.InvalidParameterException;

import w094j.ctrl8.terminal.Terminal;

/**
 * Abstract Class to encapsulate a statement, which composes of its command and
 * its parameter(s). The handling of the parameters is to be handled at its
 * subclasses.
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public abstract class Statement {

    private String arguments;
    private Command command;

    /**
     * Creates an Statement object with the Command and arguments
     *
     * @param command
     */
    protected Statement(Command command, String arguments) {
        this.command = command;
        this.arguments = arguments;
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
        switch (command) {

        /*
         * 1. add 2. delete 3. exit 4. history 5. list 6. modify 7. search
         */
            case ADD :
                statement = new AddStatement(command,
                        Command.removeCommandKeyword(statementString));
                break;
            case DELETE :
                statement = new DeleteStatement(command,
                        Command.removeCommandKeyword(statementString));
                break;
            case EXIT :
                statement = new ExitStatement(command);
                break;
            case HISTORY :
                statement = new HistoryStatement(command);
                break;
            case LIST :
                statement = new ListStatement(command);
                break;
            case MODIFY :
                statement = new ModifyStatement(command);
                break;
            case SEARCH :
                statement = new ExitStatement(command);
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
     */
    public abstract void execute(Terminal terminal);

    /**
     * @return the command
     */
    public Command getCommand() {
        return this.command;
    }

    /**
     * @param command
     *            the command to set
     */
    public void setCommand(Command command) {
        this.command = command;
    }
}