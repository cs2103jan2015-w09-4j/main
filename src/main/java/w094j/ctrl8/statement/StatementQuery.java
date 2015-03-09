package w094j.ctrl8.statement;

import java.security.InvalidParameterException;

/**
 * Abstract Class to encapsulate a statement which has parameters.
 */

//@formatter:off
//@author A0112521B
//@formatter:on

public abstract class StatementQuery extends Statement {

    /**
     * Creates a Statement with parameter, checks the statementString for any
     * parameters. If there are no parameters, fail immediately.
     *
     * @param command
     *            Command type.
     * @param statementString
     *            The statement that the user entered into the terminal.
     */
    protected StatementQuery(Command command, String statementString) {
        super(command, statementString);
        if (this.getArgumentsString().isEmpty()) {
            throw new InvalidParameterException(command
                    + " does not have any parameters.");
        }
    }

}
