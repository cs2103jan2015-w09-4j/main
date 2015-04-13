package w094j.ctrl8.parse.statement;

import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.ParameterParser;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.parse.statement.parameter.ParameterContainer;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.taskmanager.ITaskManager;

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
        ParameterParser parameterParser = Parser.getInstance()
                .getStatementParser().getParameterParser();
        Matcher queryMatcher = Pattern.compile(",").matcher(
                this.getStatementArgumentsOnly());

        if (queryMatcher.find()) {
            this.query = this.getStatementArgumentsOnly().substring(0,
                    queryMatcher.start());
        } else {
            throw new ParseException(
                    "Modify statement must contain query argument.");
        }

        this.task = new Task();
        ParameterContainer container = parameterParser.parse(this
                .getStatementArgumentsOnly().substring(queryMatcher.end()));
        container.addAll(this.task);

        logger.debug("Valid modify Command, parsed \"" + statementString
                + "\": query=\"" + this.query + "\" task="
                + new Gson().toJson(this.task));

    }

    @Override
    public Response execute(ITaskManager taskManager, boolean isUndo) {
        // Statement to be added
        return taskManager
                .modify(this.query, new Task(this.task), this, isUndo);

    }
}
