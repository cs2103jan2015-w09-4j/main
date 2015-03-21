package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.ParameterParser;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.parameter.ParameterContainer;
import w094j.ctrl8.terminal.Terminal;

import com.google.gson.Gson;

//@author A0065517A
/**
 * Class to encapsulate an add statement. Add statements must be matchable to
 * the following regex: ^add\s[A-Za-z0-9]+$ .
 */
public class AddStatement extends Statement {

    private static Logger logger = LoggerFactory.getLogger(AddStatement.class);
    private static ParameterParser parameterParser = Parser.getInstance()
            .getStatementParser().getParameterParser();

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
     * @param statementString
     *            the string to be added.
     * @throws ParseException
     *             if the parameters does not exist.
     */
    public AddStatement(String statementString) throws ParseException {
        super(CommandType.ADD, statementString);
        this.task = new Task();
        ParameterContainer container = parameterParser.parse(statementString);
        // TODO no validation rules for the statement
        container.addAll(null, this.task);
        this.task.toCompleteTask();
        if (this.task.getTaskType() == Task.TaskType.INCOMPLETE) {
            throw new ParseException(
                    "Task added must be a Complete tasks of the 3 types.");
        }
        logger.debug("Valid add Command, parsed \"" + statementString
                + "\": task=" + new Gson().toJson(this.task));
    }

    @Override
    public void execute(Terminal terminal) throws CommandExecuteException {
        terminal.add(this.task);
    }
}
