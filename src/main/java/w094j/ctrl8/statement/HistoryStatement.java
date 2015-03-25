package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.taskmanager.TaskManager;

//@author A0065517A
/**
 * History Statement has no parameters.
 */
public class HistoryStatement extends StatementNoParams {

    private static Logger logger = LoggerFactory
            .getLogger(HistoryStatement.class);

    /**
     * Creates a new history Statement.
     *
     * @param statementString
     */
    public HistoryStatement(String statementString) {
        super(CommandType.HISTORY, statementString);
    }

    @Override
    public void execute(TaskManager taskManager) throws CommandExecuteException {
        // TODO Link to Terminal
        taskManager.viewHistory();
        logger.debug("History command not implemented yet.");
    }

}
