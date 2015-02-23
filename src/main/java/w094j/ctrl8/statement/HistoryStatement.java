package w094j.ctrl8.statement;

import w094j.ctrl8.terminal.Terminal;

/**
 * History Statement has no parameters.
 *
 * @author Han Liang Wee, Eric(A0065517A)
 */
public class HistoryStatement extends StatementNoParams {

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
    }

}