package w094j.ctrl8.statement.parameter;

import java.security.InvalidParameterException;

/**
 * Priority must be a number between 0 and 10. Inclusive of both numbers.
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public class PriorityParameter extends Parameter {

    private int piority;

    /**
     * Creates a new Priority Parameter.
     *
     * @param payload
     *            to be parsed to 0 to 10.
     */
    public PriorityParameter(String payload) {
        super(ParameterSymbol.PRIORITY, payload);
        assert (payload != null);
        if (!payload.isEmpty()) {
            try {
                this.piority = Integer.parseInt(payload);
            } catch (NumberFormatException nfe) {
                throw new InvalidParameterException(
                        "Priority must be an integer.");
            }

            if ((this.piority < 0) || (this.piority > 10)) {
                throw new InvalidParameterException(
                        "Priority must be between and inclusive of 0 and 10. 0 <= priority <= 10");
            }
        } else {
            this.piority = 0;
        }

    }

    /**
     * @return the piority
     */
    public int getPiority() {
        return this.piority;
    }

    /**
     * @param piority
     *            the piority to set
     */
    public void setPiority(int piority) {
        this.piority = piority;
    }
}
