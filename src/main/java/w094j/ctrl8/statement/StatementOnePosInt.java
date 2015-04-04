package w094j.ctrl8.statement;

import java.security.InvalidParameterException;

//@author A0112521B
/**
 * Abstract Class to encapsulate a statement which has only 1 positive integer
 * parameter.
 */
public abstract class StatementOnePosInt extends Statement {

    private int positiveInteger;

    /**
     * Creates a Statement with parameter. If the parameter is not a positive
     * integer, fail immediately.
     *
     * @param command
     *            Command type.
     * @param statementString
     *            The statement that the user entered into the terminal.
     */
    protected StatementOnePosInt(CommandType command, String statementString) {
        super(command, statementString);

        try {
            this.positiveInteger = Integer.parseInt(this
                    .getStatementArgumentsOnly());
        } catch (Exception e) {
            throw new InvalidParameterException(command
                    + " takes in 1 positive integer parameter only.");
        }

        if (this.positiveInteger < 1) {
            throw new InvalidParameterException(command
                    + " takes in 1 positive integer parameter only.");
        }

    }

    /**
     * @return the positiveInteger
     */
    public int getPositiveInteger() {
        return this.positiveInteger;
    }

    /**
     * @param positiveInteger
     *            the positiveInteger to set
     */
    public void setPositiveInteger(int positiveInteger) {
        this.positiveInteger = positiveInteger;
    }

}
