package w094j.ctrl8.parse.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112521B
public class AliasDeleteStatement extends StatementQuery {

    private static Logger logger = LoggerFactory.getLogger(DoneStatement.class);
    private String query;

    /**
     * @param statementString
     */
    public AliasDeleteStatement(String statementString) {
        super(CommandType.ALIAS_DELETE, statementString);
        this.query = this.getStatementArgumentsOnly();
        logger.debug("Valid alias-delete Command, query \"" + statementString
                + "\"");
    }

    @Override
    public Response execute(ITaskManager taskManager, boolean isUndo)
            throws DataException {
        // TODO Link to Terminal
        return taskManager.aliasDelete(this.query, this, isUndo);

    }

}