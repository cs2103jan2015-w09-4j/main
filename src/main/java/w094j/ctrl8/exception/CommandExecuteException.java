package w094j.ctrl8.exception;

//@author A0110787A

/*
 * Exception class thrown for any errors encountered in execution when Statement object calls .execute()
 */
public class CommandExecuteException extends Exception {
    // Parameterless constructor
    public CommandExecuteException() {
    }

    // Constructor that accepts a String message
    public CommandExecuteException(String message) {
        super(message);
    }
}
