package w094j.ctrl8.parse.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112521B
public class DeleteStatement extends StatementQuery {

    private static Logger logger = LoggerFactory
            .getLogger(DeleteStatement.class);
    private String query;

    /**
     * @param statementString
     */
    public DeleteStatement(String statementString) {
        super(CommandType.DELETE, statementString);
        this.query = this.getStatementArgumentsOnly();
        logger.debug("Valid delete Command, query \"" + statementString + "\"");
    }

    @Override
    public void execute(ITaskManager taskManager)
            throws CommandExecuteException {

        taskManager.delete(this.query, this);
    }

}