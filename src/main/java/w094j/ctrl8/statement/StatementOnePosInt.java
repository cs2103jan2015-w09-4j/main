package w094j.ctrl8.statement;

import java.security.InvalidParameterException;

//@author A0112521B
/**
 * Abstract Class to encapsulate a statement which has only 1 positive integer
 * parameter.
 */
public abstract class StatementOnePosInt extends Statement {

    /**
     * Creates a Statement with parameter. If the parameter is not a positive
     * integer, fail immediately.
     *
     * @param command
     *            Command type.
     * @param statementString
     *            The statement that the user entered into the terminal.
     */
    protected StatementOnePosInt(Command command, String statementString) {
        super(command, statementString);
        int index;

        try {
            index = Integer.parseInt(this.getArgumentsString());
        } catch (Exception e) {
            throw new InvalidParameterException(command
                    + " takes in 1 positive integer parameter only.");
        }

        if (index < 1) {
            throw new InvalidParameterException(command
                    + " takes in 1 positive integer parameter only.");
        }

    }

}
