package w094j.ctrl8.parse.statement;


import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@author A0112521B
/**
 * Abstract Class to encapsulate a statement which has only 1 positive integer
 * parameter.
 */
public abstract class StatementOnePosInt extends Statement {

    private int positiveInteger;
    private static Logger logger = LoggerFactory.getLogger(StatementOnePosInt.class);
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
            logger.debug(this.getStatementArgumentsOnly()+ "int");
            
            this.positiveInteger = Integer.parseInt(this
                    .getStatementArgumentsOnly().trim());
            
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
