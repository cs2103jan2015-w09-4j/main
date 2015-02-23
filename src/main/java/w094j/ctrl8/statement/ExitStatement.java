package w094j.ctrl8.statement;

import w094j.ctrl8.terminal.Terminal;

/**
 * Class to encapsulate an exit statement.
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public class ExitStatement extends StatementNoParams {

    /**
     * Creates a new exit Statement.
     *
     * @param statementString
     */
    public ExitStatement(String statementString) {
        super(Command.EXIT, statementString);
    }

    @Override
    public void execute(Terminal terminal) {
        // TODO Link to Terminal
    }

}
