package w094j.ctrl8.parse.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.parse.CommandParser;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0065517A
/**
 * Abstract Class to encapsulate a statement, which composes of its command and
 * its parameter(s). The handling of the parameters is to be handled at its
 * subclasses.
 */
public abstract class Statement {

    private static Logger logger = LoggerFactory.getLogger(Statement.class);

    // command of the statement
    private CommandType command;
    // without command, arguments only
    private String statementArgumentsOnly;

    /**
     * Creates an Statement object with the Command and arguments
     *
     * @param command
     */
    protected Statement(CommandType command, String statementString) {
        this.command = command;
        CommandParser commandParser = Parser.getInstance().getStatementParser()
                .getCommandParser();
        this.statementArgumentsOnly = commandParser
                .removeCommandKeyword(statementString);
        logger.debug("Statement initialized with Command(" + this.command
                + ") and arguments " + this.statementArgumentsOnly);
    }

    /**
     * Executes the command in the specified terminal, performing the specific
     * command related to the respective commands.
     *
     * @param taskManager
     *            to execute the command in.
     * @throws CommandExecuteException
     *             when the execution of the command has problems.
     */
    public abstract Response execute(ITaskManager taskManager, boolean isUndo);

    /**
     * @return the command
     */
    public CommandType getCommand() {
        return this.command;
    }

    /**
     * @return the statementArgumentsOnly
     */
    public String getStatementArgumentsOnly() {
        return this.statementArgumentsOnly;
    }

    /**
     * @param command
     *            the command to set
     */
    public void setCommand(CommandType command) {
        this.command = command;
    }

    /**
     * @param statementArgumentsOnly
     *            the statementArgumentsOnly to set
     */
    public void setStatementArgumentsOnly(String statementArgumentsOnly) {
        this.statementArgumentsOnly = statementArgumentsOnly;
    }

}
