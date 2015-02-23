package w094j.ctrl8.statement;

import w094j.ctrl8.terminal.Terminal;

/**
 * Class to encapsulate a display statement.
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public class DisplayStatement extends Statement {

    /**
     * @param command
     */
    public DisplayStatement(Command command) {
        super(command);
        assert (Command.LIST == command);
    }

    @Override
    public void execute(Terminal terminal) {
// terminal.display();
    }

}
