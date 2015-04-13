package w094j.ctrl8.parse.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112521B
public class HistoryUndoStatement extends StatementOnePosInt {

    private static Logger logger = LoggerFactory
            .getLogger(HistoryUndoStatement.class);

    /**
     * @param statementString
     */
    public HistoryUndoStatement(String statementString) {
        super(CommandType.HISTORY_UNDO, statementString);
        logger.debug("Valid history-undo Command, query \"" + statementString
                + "\"");
    }

    @Override
    public Response execute(ITaskManager taskManager, boolean isUndo) {
        // TODO Link to Terminal
        // temporary index to be passed before search implemented.
        return taskManager.historyUndo(this.getPositiveInteger(), this);
    }
}
