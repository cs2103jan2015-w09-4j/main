package w094j.ctrl8.exception;

/**
 * Parse Exception is thrown when there is a problem with Parsing.
 *
 * @author Han Liang Wee, Eric(A0065517A)
 */
public class ParameterParseException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public ParameterParseException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param throwable
     */
    public ParameterParseException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
