package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0065517A
/**
 * Represents a Search Statement
 */
public class SearchStatement extends StatementQuery {

    private static Logger logger = LoggerFactory
            .getLogger(SearchStatement.class);
    private String query;
    private Task task;// TODO Implementation not complete

    /**
     * @param statementString
     */
    public SearchStatement(String statementString) {
        super(CommandType.SEARCH, statementString);
        this.query = this.getStatementArgumentsOnly();
        logger.debug("Valid search Command, query \"" + statementString + "\"");
    }

    @Override
    public void execute(ITaskManager taskManager)
            throws CommandExecuteException {
        // Statement to be added
        taskManager.search(this.query, this.task);
    }

}