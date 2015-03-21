package w094j.ctrl8.exception;

//@author A0065517
/**
 * Exception object thrown when there are problems with the configuration
 * options.
 */
public class IllegalConfigException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public IllegalConfigException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param throwable
     */
    public IllegalConfigException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
