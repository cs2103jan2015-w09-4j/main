package w094j.ctrl8.parse.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0065517A
/**
 * Represents a Search Statement
 */
public class SearchStatement extends StatementQuery {

    private static Logger logger = LoggerFactory
            .getLogger(SearchStatement.class);
    private String query;

    /**
     * @param statementString
     */
    public SearchStatement(String statementString) {
        super(CommandType.SEARCH, statementString);
        this.query = this.getStatementArgumentsOnly();
        logger.debug("Valid search Command, query \"" + statementString + "\"");
    }

    @Override
    public Response execute(ITaskManager taskManager, boolean isUndo)
            throws DataException {
        // Statement to be added
        return taskManager.search(this.query, this);
    }

}