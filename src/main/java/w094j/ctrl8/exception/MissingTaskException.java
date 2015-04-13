package w094j.ctrl8.exception;

//@author A0112092W
/**
 * Exception class thrown when a task is not found on the taskMap This is to
 * ensure that we do not accidentally modify other task or add a new task to the
 * taskMap
 */
public class MissingTaskException extends Exception {
    // Parameterless constructor

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public MissingTaskException() {
    }

    // Constructor that accepts a String message
    /**
     * @param message
     */
    public MissingTaskException(String message) {
        super(message);
    }
}
