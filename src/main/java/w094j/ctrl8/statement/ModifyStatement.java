package w094j.ctrl8.statement;

import java.security.InvalidParameterException;

import w094j.ctrl8.terminal.Terminal;

/**
 * Class to encapsulate an add statement. Add statements must be matchable to
 * the following regex: ^add\s[A-Za-z0-9]+$ .
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public class ModifyStatement extends Statement {

    private String toAdd;

    /**
     * Initializes an add statement, ensures that the statement is valid.
     *
     * @param command
     *            add command.
     * @param toAdd
     *            the string to be added.
     * @exception InvalidParameterException
     *                if the parameters does not exist.
     */
    public ModifyStatement(Command command) {
        super(command, this.toAdd);
        assert (Command.ADD == command);
        if ((this.toAdd == null) || (this.toAdd.length() == 0)) {
            throw new InvalidParameterException(
                    "add command must have valid parameters.");
        }
        this.toAdd = this.toAdd;
    }

    @Override
    public void execute(Terminal terminal) {
// terminal.add(this.toAdd);
    }
}
