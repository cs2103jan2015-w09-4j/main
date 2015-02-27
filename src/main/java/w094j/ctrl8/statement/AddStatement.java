package w094j.ctrl8.statement;

import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.parameter.ParameterContainer;
import w094j.ctrl8.statement.parameter.ParameterSymbol;
import w094j.ctrl8.terminal.Terminal;

import com.google.gson.Gson;

/**
 * Class to encapsulate an add statement. Add statements must be matchable to
 * the following regex: ^add\s[A-Za-z0-9]+$ .
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public class AddStatement extends Statement {

    private static Logger logger = LoggerFactory.getLogger(AddStatement.class);

    private Task task;

    /**
     * Initializes an add statement, ensures that the add statement is a valid
     * statement, the task that is added must either be:
     *
     * <pre>
     * 1. Floating Task
     * 2. Deadline Task
     * 3. Timed Task(Event)
     * </pre>
     *
     * Each would also have the option to specify a recursion.
     *
     * @param arguments
     *            the string to be added.
     * @exception InvalidParameterException
     *                if the parameters does not exist.
     */
    public AddStatement(String statementString) {
        super(Command.ADD, statementString);
        this.task = new Task();
        ParameterContainer container = ParameterSymbol.parse(statementString);
        // TODO no validation rules for the statement
        container.addAll(null, this.task);
        this.task.toCompleteTask();
        logger.debug("Valid add Command, parsed \"" + statementString
                + "\": task=" + new Gson().toJson(this.task));
    }

    @Override
    public void execute(Terminal terminal) throws CommandExecuteException {
        terminal.add(this.task);
    }
}
