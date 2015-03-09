package w094j.ctrl8.message;

//@author A0112521B

public class HelpMessage {

    public static final int ADD_END_INDEX = 8;
    public static final int ADD_START_INDEX = 1;
    public static final int ALIAS_ADD_INDEX = 20;
    public static final int ALIAS_DELETE_INDEX = 21;
    public static final int ALIAS_INDEX = 19;
    public static final int DELETE_INDEX = 10;
    public static final int DONE_INDEX = 9;
    public static final int EXIT_INDEX = 26;
    public static final int HISTORY_CLEAR_INDEX = 16;
    public static final int HISTORY_INDEX = 15;
    public static final int HISTORY_UNDO_INDEX = 17;
    public static final int MODIFY_INDEX = 13;
    public static final int SEARCH_INDEX = 12;
    public static final String[][] TABLE = {
        { "Topic", "Command", "Example" },
        { "Add a New Task   (title)", "add ={<title>}", "add ={UROP Meeting}" },
        { "                 (category)", "#{<category>}", "#{school}" },
        { "                 (description)", "+{<description>}","+{Remember to talk about Crawler technologies.}" },      
        { "                 (start date)", "-{<date>}", "-{today 1pm}" },
        { "                 (end date)", "~{<date>}", "~{today 3pm}" },
        { "                 (location)", "@{<location>}", "@{Prof. Martin Henz Office}" },
        { "                 (priority)", "%{<priority number(0~10)>}", "%{10}" },
        { "                 (reminder)", "!{<date>}", "!{today 12pm}" },
        { "Mark a Task as Done", "done <title>", "done UROP Meeting" },
        { "Delete a Task", "delete <title>", "delete UROP Meeting" },
        { "View All Tasks", "view", "-" },
        { "Search for a Task", "search <title>", "search UROP Meeting" },
        { "Modify a Task", "modify <title> <changes>","modify UROP Meeting @{UTown Seminar Room 8}" },
        { "", "", "" },
        { "View History Actions", "history", "-" },
        { "Clear a History Action", "history-clear <number>", "history-clear 2" },
        { "Undo a History Action", "history-undo <number>","history-undo 1" },
        { "", "", "" },
        { "View All Alias", "alias", "-" },
        { "Add a New Alias", "alias-add <alias> <phrase>", "alias-add nus National University of Singapore" },
        { "Delete an Alias", "alias-delete <alias>", "alias-delete nus" },
        { "", "", "" },
        { "Help - View All Commands", "help", "-" },
        { "Help - View a Specific Command", "help <command>", "help add" },
        { "", "", "" }, 
        { "Exit the Program", "exit", "-" } };

    public static final int VIEW_INDEX = 11;

}
