package w094j.ctrl8.parse.statement;

import java.security.InvalidParameterException;

//@author A0065517A
/**
 * Abstract Class to encapsulate a statement which has no parameters.
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
    public StatementNoParams(CommandType command, String statementString) {
        super(command, statementString);
        if (!this.getStatementArgumentsOnly().isEmpty()) {
            throw new InvalidParameterException(command
                    + " does not take in any parameters.");
        }
    }
}
