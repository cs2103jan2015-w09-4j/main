package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.taskmanager.TaskManager;

//@author A0112521B
public class HistoryUndoStatement extends StatementOnePosInt {

    private static Logger logger = LoggerFactory
            .getLogger(HistoryUndoStatement.class);
    private String query;

    /**
     * @param statementString
     */
    public HistoryUndoStatement(String statementString) {
        super(CommandType.HISTORY_UNDO, statementString);
        this.query = this.getStatementArgumentsOnly();
        logger.debug("Valid history-undo Command, query \"" + statementString
                + "\"");
    }

    @Override
    public void execute(TaskManager taskManager) throws CommandExecuteException {
        // TODO Link to Terminal
        taskManager.historyUndo(this.query);
        logger.debug("historyUndo not implemented yet.");
    }
}
