package w094j.ctrl8.message;

//@author A0112092W
/**
 * Class encapsulates all information that is pulled and/or pushed from an
 * external file. Examples include interacting with a local file. Or dumping
 * statement history into an output file. TODO: Cater for Google integration
 */

public class NormalMessage {
    public static final String ADD_TASK_SUCCESSFUL = " added successfully!";
    public static final String APP_NAME = "Ctrl-8";
    public static final String DELETE_TASK_SUCCESSFUL = " deleted successfully!";
    public static final String DELETED_TASK_HISTORY = "History of Deleted Task: ";
    public static final String DISPLAY_NEXT_COMMAND_REQUEST = "Next command: ";
    public static final String EXIT_COMMAND = "Thank you for using " + APP_NAME;

    //@author A0110787A
    public static final String HELP_ADD_COMMAND_SYNTAX = "add ={<title>}";
    public static final String HELP_ALL = NormalMessage.HELP_HEADER
            + NormalMessage.HELP_ADD_COMMAND_SYNTAX + "\n"
            + NormalMessage.HELP_DELETE_COMMAND_SYNTAX + "\n"
            + NormalMessage.HELP_MODIFY_COMMAND_SYNTAX + "\n"
            + NormalMessage.HELP_VIEW_COMMAND_SYNTAX + "\n";
    public static final String HELP_DELETE_COMMAND_SYNTAX = "delete <query>";
    public static final String HELP_HEADER = "List of supported commands: \n";
    public static final String HELP_MODIFY_COMMAND_SYNTAX = "modify <query> ={<title>}..";

    public static final String HELP_VIEW_COMMAND_SYNTAX = "view";
    public static final String MODIFY_TASK_SUCCESSFUL = " modified successfully!";
    public static final String NO_FILEPATH_MESSAGE = "No filepath entered. Using default filepath.";
    public static final String NO_TASK_FOUND = "No Tasks found.";

    //@author A0112092W
    public static final String START_MESSAGE = "Starting Ctrl-8...";
    public static final String WELCOME_MESSAGE = "Welcome for using Ctrl-8!";
    public static final String DONE_TASK_SUCCESSFUL = " is done!";
    public static final String NO_HISTORY_FOUND = "No History Found";
    public static final String HISTORY_CLEAR_SUCCESSFUL = "The following action history is deleted successfully!";
    public static final String ALIAS_MAP_EMPTY = "There is no alias.";
    public static final String ADD_ALIAS_SUCCESSFUL = " is successfully alias to ";
    public static final String ALIAS_DELETE_SUCCESSFUL = "The following alias is deleted successfully!";

}
