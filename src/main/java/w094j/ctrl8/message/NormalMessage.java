package w094j.ctrl8.message;

/**
 * Class encapsulates all information that is pulled and/or pushed from an
 * external file. Examples include interacting with a local file. Or dumping
 * statement history into an output file. TODO: Cater for Google integration
 */

/**
 * @author Lin Chen-Hsin
 */
public class NormalMessage {
    public static final String ADD_TASK_SUCCESSFUL = " added successfully!";
    public static final String DELETE_TASK_SUCCESSFUL = " deleted successfully!";
    public static final String DELETED_TASK_HISTORY = "History of Deleted Task: ";
    public static final String DISPLAY_NEXT_COMMAND_REQUEST = "Next command: ";
    public static final String EXIT_COMMAND = "Thank you for using Ctrl Infinity!";

    // @author A0110787A
    public static final String HELP_ADD_COMMAND_SYNTAX = "add ={<title>} $";
    public static final String HELP_DELETE_COMMAND_SYNTAX = "delete <query>";
    public static final String HELP_HEADER = "List of supported commands: \n";
    public static final String HELP_MODIFY_COMMAND_SYNTAX = "modify <query> ={<title>}..";
    public static final String HELP_VIEW_COMMAND_SYNTAX = "view";

    public static final String MODIFY_TASK_SUCCESSFUL = " modified successfully!";
    public static final String NO_TASK_FOUND = "No Tasks found.";

    // @author A0110787A, A0112092W
    public static final String START_MESSAGE = "Starting Ctrl-8...";
    public static final String WELCOME_MESSAGE = "Welcome for using Ctrl-8!";

}
