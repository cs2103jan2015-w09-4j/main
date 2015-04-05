package w094j.ctrl8.parse.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
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
    public void execute(ITaskManager taskManager)
            throws CommandExecuteException {
        // TODO Link to Terminal
        // temporary index to be passed before search implemented.
        taskManager.historyUndo(this.getPositiveInteger());
    }
}
