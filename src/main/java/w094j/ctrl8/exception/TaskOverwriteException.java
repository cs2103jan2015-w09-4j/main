package w094j.ctrl8.exception;

//@author A0110787A

/*
 * Exception class thrown when an attempt to overwrite an existing task in a Database occurs
 * This is to ensure that we do not accidentally overwrite a Task with a Task delta resulting in loss of information
 */
public class TaskOverwriteException extends Exception {
    // Parameterless constructor
    public TaskOverwriteException() {
    }

    // Constructor that accepts a String message
    public TaskOverwriteException(String message) {
        super(message);
    }
}
