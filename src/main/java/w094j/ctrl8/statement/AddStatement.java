package w094j.ctrl8.statement;

import java.security.InvalidParameterException;
import java.util.List;

import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.parameter.Parameter;
import w094j.ctrl8.statement.parameter.ParameterSymbol;
import w094j.ctrl8.terminal.Terminal;

/**
 * Class to encapsulate an add statement. Add statements must be matchable to
 * the following regex: ^add\s[A-Za-z0-9]+$ .
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public class AddStatement extends Statement {

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
        List<Parameter> parameterList = ParameterSymbol.parse(statementString);
        for (Parameter eaParameter : parameterList) {
            eaParameter.add(this.task);
        }
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.add(this.task);
    }
}
