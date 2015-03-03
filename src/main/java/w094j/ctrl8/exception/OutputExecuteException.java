package w094j.ctrl8.exception;

public class OutputExecuteException extends Exception {
    // Parameterless constructor
    public OutputExecuteException() {
    }

    // Constructor that accepts a String message
    public OutputExecuteException(String message) {
        super(message);
    }

}
