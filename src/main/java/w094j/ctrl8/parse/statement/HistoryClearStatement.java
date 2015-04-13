package w094j.ctrl8.parse.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112521B
public class HistoryClearStatement extends StatementOnePosInt {

    private static Logger logger = LoggerFactory
            .getLogger(HistoryClearStatement.class);
    private int query;

    /**
     * @param statementString
     */
    public HistoryClearStatement(String statementString) {
        super(CommandType.HISTORY_CLEAR, statementString);
        this.query = this.getPositiveInteger();
        logger.debug("Valid history-clear Command, query \"" + statementString
                + "\"");
    }

    @Override
    public Response execute(ITaskManager taskManager, boolean isUndo) {
        return taskManager.historyClear(this.query, this);
    }

}
