package w094j.ctrl8.message;

/**
 * Class encapsulates all information that is pulled and/or pushed from an
 * external file. Examples include interacting with a local file. Or dumping
 * statement history into an output file. TODO: Cater for Google integration
 */

/**
 * @author Lin Chen-Hsin Chen Tze Cheng
 */
public class ErrorMessage {
    public static final String ERROR = "Error: ";
    public static final String ERROR_READING_INPUT = "Error: User Input Failed!";
    public static final String OPTION_NOT_FOUND = "Error: Option can't be found";
    public static final String STATEMENT_ADD_PARAMETER = "Add statement must have parameters.";
    public static final String TASK_KEY_ALREADY_EXISTS = "Error: Task key already exists!";
    public static final String TASK_KEY_DOES_NOT_EXIST = "Error: Task key does not exist!";

}