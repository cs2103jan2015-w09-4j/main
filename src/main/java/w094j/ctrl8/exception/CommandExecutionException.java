package w094j.ctrl8.exception;

//@author A0110787A

/*
 * Exception class thrown for any errors encountered in execution when Statement object calls .execute()
 */
public class CommandExecutionException extends Exception {
    // Parameterless constructor
    public CommandExecutionException() {
    }

    // Constructor that accepts a String message
    public CommandExecutionException(String message) {
        super(message);
    }
}
