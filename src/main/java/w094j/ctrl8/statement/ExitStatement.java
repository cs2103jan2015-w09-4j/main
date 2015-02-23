package w094j.ctrl8.statement;

import w094j.ctrl8.terminal.Terminal;

/**
 * Class to encapsulate an exit statement.
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public class ExitStatement extends Statement {

    /**
     * @param command
     */
    public ExitStatement(Command command) {
        super(command);
        assert (Command.EXIT == command);
    }

    @Override
    public void execute(Terminal terminal) {
// terminal.exit();

    }

}
