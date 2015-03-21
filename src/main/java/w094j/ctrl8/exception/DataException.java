package w094j.ctrl8.exception;

/**
 * Exception object thrown when there is a problem with Data Structure
 * operation.
 */
public class DataException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public DataException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param throwable
     */
    public DataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
