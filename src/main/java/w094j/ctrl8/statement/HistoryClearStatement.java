package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B
public class HistoryClearStatement extends StatementOnePosInt {

    private static Logger logger = LoggerFactory
            .getLogger(HistoryClearStatement.class);
    private String query;

    /**
     * @param statementString
     */
    public HistoryClearStatement(String statementString) {
        super(CommandType.HISTORY_CLEAR, statementString);
        this.query = this.getStatementArgumentsOnly();
        logger.debug("Valid history-clear Command, query \"" + statementString
                + "\"");
    }

    @Override
    public void execute(Terminal terminal) throws CommandExecuteException {
        // TODO Link to Terminal
        // terminal.historyClear(this.query);
        logger.debug("historyClear not implemented yet.");
    }

}
