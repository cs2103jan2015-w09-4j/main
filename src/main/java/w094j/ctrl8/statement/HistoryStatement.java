package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.terminal.Terminal;

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
        super(Command.HISTORY, statementString);
    }

    @Override
    public void execute(Terminal terminal) {
        // TODO Link to Terminal
        logger.debug("History command not implemented yet.");
    }

}
