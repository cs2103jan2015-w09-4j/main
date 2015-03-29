package w094j.ctrl8.statement;

import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.ParameterParser;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.parameter.ParameterContainer;
import w094j.ctrl8.taskmanager.TaskManager;

import com.google.gson.Gson;

//@author A0065517A
/**
 * Class to encapsulate a modify statement. Essentially a modify statement is an
 * add statement with a query. The syntax must be in the form:
 *
 * <pre>
 * \<modify\> \<query\> \<parameters\>
 * </pre>
 */
public class ModifyStatement extends Statement {

    private static Logger logger = LoggerFactory
            .getLogger(ModifyStatement.class);
    private static ParameterParser parameterParser = Parser.getInstance()
            .getStatementParser().getParameterParser();

    private String query;

    private Task task;

    /**
     * Initializes a modify statement, parses the query first.
     *
     * @param statementString
     * @throws ParseException
     * @exception InvalidParameterException
     *                if the parameters does not exist.
     */
    public ModifyStatement(String statementString) throws ParseException {
        super(CommandType.MODIFY, statementString);

        Matcher queryMatcher = Pattern.compile(",").matcher(statementString);
        logger.debug(statementString);
        if (queryMatcher.find()) {
            this.query = queryMatcher.group();
        } else {
            throw new ParseException(
                    "Modify statement must contain query argument.");
        }

        this.task = new Task();
        ParameterContainer container = parameterParser.parse(this
                .getStatementArgumentsOnly().substring(queryMatcher.end()));
        // TODO no validation rules for the statement
        container.addAll(null, this.task);

        logger.debug("Valid modify Command, parsed \"" + statementString
                + "\": query=\"" + this.query + "\" task="
                + new Gson().toJson(this.task));

    }

    @Override
    public void execute(TaskManager taskManager) throws CommandExecuteException {
        // Statement to be added
        taskManager.modify(this.query, this.task, this);

    }
}
