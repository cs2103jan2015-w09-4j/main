package w094j.ctrl8.parse.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112521B
public class DoneStatement extends StatementQuery {

    private static Logger logger = LoggerFactory.getLogger(DoneStatement.class);
    private String query;

    /**
     * @param statementString
     */
    public DoneStatement(String statementString) {
        super(CommandType.DONE, statementString);
        this.query = this.getStatementArgumentsOnly().trim();
        logger.debug("Valid done Command, query \"" + statementString + "\"");
    }

    @Override
    public Response execute(ITaskManager taskManager, boolean isUndo)
            throws CommandExecuteException, DataException {
        // TODO Link to Terminal
        // statement to be added
        return taskManager.done(this.query, this, isUndo);

    }

}