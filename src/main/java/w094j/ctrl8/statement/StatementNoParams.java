package w094j.ctrl8.statement;

import java.security.InvalidParameterException;

/**
 * Abstract Class to encapsulate a statement which has no parameters.
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public abstract class StatementNoParams extends Statement {

    /**
     * Creates a Statement without parameters, checks the statementString for
     * any parameters. If there are parameters, fail immediately.
     *
     * @param command
     *            Command type.
     * @param statementString
     *            The statement that the user entered into the terminal.
     */
    public StatementNoParams(Command command, String statementString) {
        super(command, statementString);
        if (!this.getArgumentsString().isEmpty()) {
            throw new InvalidParameterException(command
                    + " does not take in any parameters.");
        }
    }
}