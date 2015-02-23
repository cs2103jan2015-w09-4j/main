package w094j.ctrl8.statement;

import java.security.InvalidParameterException;

import w094j.ctrl8.terminal.Terminal;

/**
 * Class to encapsulate a delete statement.
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public class DeleteStatement extends Statement {

    private int lineNumber;

    /**
     * Initializes a delete statement, taking in and parsing the line number.
     *
     * @param command
     *            delete command.
     * @param lineNumberString
     *            line number to delete.
     * @exception InvalidParameterException
     *                if the lineNumber is not a proper integer.
     */
    public DeleteStatement(Command command, String lineNumberString) {
        super(command);
        assert (Command.DELETE == command);
        try {
            this.lineNumber = Integer.parseInt(lineNumberString, 10);
        } catch (NumberFormatException nfe) {
            throw new InvalidParameterException(
                    "delete command's parameters must be an integer.");
        }
    }

    @Override
    public void execute(Terminal terminal) {
// terminal.delete(this.lineNumber);
    }
}
