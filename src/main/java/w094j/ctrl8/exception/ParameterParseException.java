package w094j.ctrl8.exception;

//@author A0065517A
/**
 * Parse Exception is thrown when there is a problem with Parsing.
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
