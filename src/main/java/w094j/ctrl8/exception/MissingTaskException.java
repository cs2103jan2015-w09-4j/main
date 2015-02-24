package w094j.ctrl8.exception;

//@author A0110787A

/*
 * Exception class thrown when an attempt to modify a non-existing task in the Database occurs
 * This is to ensure that modifications to the database are done correctly, and not mixed up with adding of a new Task
 */
public class MissingTaskException extends Exception {
    // Parameterless constructor
    public MissingTaskException() {
    }

    // Constructor that accepts a String message
    public MissingTaskException(String message) {
        super(message);
    }
}
