package w094j.ctrl8.parse.statement;

import java.security.InvalidParameterException;
//@author A0112521B
/**
 * Abstract Class to encapsulate a statement which has parameters.
 */
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
    protected StatementQuery(CommandType command, String statementString) {
        super(command, statementString);
        if (this.getStatementArgumentsOnly().isEmpty()) {
            throw new InvalidParameterException(command
                    + " does not have any parameters.");
        }
    }

}
