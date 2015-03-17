package w094j.ctrl8.display;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.OutputExecuteException;
import w094j.ctrl8.message.HelpMessage;
import w094j.ctrl8.message.MagicNumbersAndConstants;
import w094j.ctrl8.message.OuputExecuteMessage;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.Command;

/**
 * Class implements Display Interface as a simple CLI How to use: To get
 * userinput as a String, call CLIDisplay.getUserInput() To display an output,
 * call CLIDisplay.outputMessage(message)
 */

//@author A0112092W
public class CLIDisplay implements IDisplay {
    private static Logger logger = LoggerFactory.getLogger(CLIDisplay.class);
    private BufferedReader br;
    private InputStream inStream;

    //@author A0110787A
    private String lastMessage;

    //@author A0112092W
    /**
     * Public constructor for a CLI Display
     */
    public CLIDisplay() {
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * This method is used to print a table with format of following the format
     * of right justified table x xxx yyy y zz zz
     *
     * @param table
     */
    private static void printTable(String[][] table) {
        // Find out what the maximum number of columns is in any row
        int maxColumns = 0;
        for (String[] element : table) {
            maxColumns = Math.max(element.length, maxColumns);
        }

        // Find the maximum length of a string in each column
        int[] lengths = new int[maxColumns];
        for (String[] element : table) {
            for (int j = 0; j < element.length; j++) {
                lengths[j] = Math.max(element[j].length(), lengths[j]);
            }
        }

        // Generate a format string for each column
        String[] formats = new String[lengths.length];
        for (int i = 0; i < lengths.length; i++) {
            formats[i] = "%1$" + lengths[i] + "s"
                    + ((i + 1) == lengths.length ? "\n" : " ");
        }

        // Print 'em out
        for (String[] element : table) {
            for (int j = 0; j < element.length; j++) {
                System.out.printf(formats[j], element[j]);
            }
        }
    }

    /**
     * This method is used to print the help table. (modified from printTable)
     */
    //@author A0112521B
    private static void printTableWithBorder(int startIndex, int endIndex,
            String[][] table) {
        char borderKnot = '+';
        char horizontalBorder = '-';
        char verticalBorder = '|';
        int spaceInfront = 1;
        int spaceBehind = 2;

        // Find out what the maximum number of columns is in any row
        int maxColumns = 0;
        for (String[] element : table) {
            maxColumns = Math.max(element.length, maxColumns);
        }

        // Find the maximum length of a string in each column
        int[] lengths = new int[maxColumns];
        for (int j = 0; j < maxColumns; j++) {
            lengths[j] = Math.max(table[0][j].length(), lengths[j]);
        }
        for (int i = startIndex; i <= endIndex; i++) {
            for (int j = 0; j < maxColumns; j++) {
                lengths[j] = Math.max(table[i][j].length(), lengths[j]);
            }
        }

        for (int j = 0; j < maxColumns; j++) {
            lengths[j] += spaceBehind;
        }

        // Print header
        for (int i = 0; i < maxColumns; i++) {
            System.out.print(borderKnot);
            for (int j = 0; j < (lengths[i] + spaceInfront); j++) {
                System.out.print(horizontalBorder);
            }
        }
        System.out.println(borderKnot);
        for (int i = 0; i < maxColumns; i++) {
            System.out.print(verticalBorder);
            for (int k = 0; k < spaceInfront; k++) {
                System.out.print(" ");
            }
            System.out.print(table[0][i]);
            for (int j = 0; j < (lengths[i] - table[0][i].length()); j++) {
                System.out.print(" ");
            }
        }
        System.out.println(verticalBorder);
        for (int i = 0; i < maxColumns; i++) {
            System.out.print(borderKnot);
            for (int j = 0; j < (lengths[i] + spaceInfront); j++) {
                System.out.print(horizontalBorder);
            }
        }
        System.out.println(borderKnot);

        // Print content (from startIndex to endIndex)

        for (int i = startIndex; i <= endIndex; i++) {
            System.out.print(verticalBorder);
            for (int j = 0; j < maxColumns; j++) {
                for (int k = 0; k < spaceInfront; k++) {
                    System.out.print(" ");
                }
                System.out.print(table[i][j]);
                for (int k = 0; k < (lengths[j] - table[i][j].length()); k++) {
                    System.out.print(" ");
                }
                System.out.print(verticalBorder);
            }
            System.out.println("");

        }
        for (int i = 0; i < maxColumns; i++) {
            System.out.print(borderKnot);
            for (int j = 0; j < (lengths[i] + spaceInfront); j++) {
                System.out.print(horizontalBorder);
            }
        }
        System.out.println(borderKnot);

    }

    //@author A0110787A
    /*
     * For testing purposes. Facilitates JUnit testing of individual modules
     * using CLIDisplay
     */
    public String getLastMessage() {
        return this.lastMessage;

    }

    //@author A0112092W
    @Override
    public InputStream getInputStream() {

        return this.inStream;
    }

    /**
     * @param command
     */
    //@author A0112521B
    public void outputHelpMessage(Command command) {
        if ((command == null) || (command == Command.HELP)) {
            printTableWithBorder(1, HelpMessage.EXIT_INDEX, HelpMessage.TABLE);
        } else {

            switch (command) {
                case ADD :
                    printTableWithBorder(HelpMessage.ADD_START_INDEX,
                            HelpMessage.ADD_END_INDEX, HelpMessage.TABLE);
                    break;
                case ALIAS :
                    printTableWithBorder(HelpMessage.ALIAS_INDEX,
                            HelpMessage.ALIAS_INDEX, HelpMessage.TABLE);
                    break;
                case ALIAS_ADD :
                    printTableWithBorder(HelpMessage.ALIAS_ADD_INDEX,
                            HelpMessage.ALIAS_ADD_INDEX, HelpMessage.TABLE);
                    break;
                case ALIAS_DELETE :
                    printTableWithBorder(HelpMessage.ALIAS_DELETE_INDEX,
                            HelpMessage.ALIAS_DELETE_INDEX, HelpMessage.TABLE);
                    break;
                case DELETE :
                    printTableWithBorder(HelpMessage.DELETE_INDEX,
                            HelpMessage.DELETE_INDEX, HelpMessage.TABLE);
                    break;
                case DONE :
                    printTableWithBorder(HelpMessage.DONE_INDEX,
                            HelpMessage.DONE_INDEX, HelpMessage.TABLE);
                    break;
                case EXIT :
                    printTableWithBorder(HelpMessage.EXIT_INDEX,
                            HelpMessage.EXIT_INDEX, HelpMessage.TABLE);
                    break;
                case HISTORY :
                    printTableWithBorder(HelpMessage.HISTORY_INDEX,
                            HelpMessage.HISTORY_INDEX, HelpMessage.TABLE);
                    break;
                case HISTORY_CLEAR :
                    printTableWithBorder(HelpMessage.HISTORY_CLEAR_INDEX,
                            HelpMessage.HISTORY_CLEAR_INDEX, HelpMessage.TABLE);
                    break;
                case HISTORY_UNDO :
                    printTableWithBorder(HelpMessage.HISTORY_UNDO_INDEX,
                            HelpMessage.HISTORY_UNDO_INDEX, HelpMessage.TABLE);
                    break;
                case MODIFY :
                    printTableWithBorder(HelpMessage.MODIFY_INDEX,
                            HelpMessage.MODIFY_INDEX, HelpMessage.TABLE);
                    break;
                case SEARCH :
                    printTableWithBorder(HelpMessage.SEARCH_INDEX,
                            HelpMessage.SEARCH_INDEX, HelpMessage.TABLE);
                    break;
                case VIEW :
                    printTableWithBorder(HelpMessage.VIEW_INDEX,
                            HelpMessage.VIEW_INDEX, HelpMessage.TABLE);
                    break;

                default :
                    assert (false);

            }
        }

    }

    public void outputMessage(String message) {
        //@author A0110787A
        /*
         * For testing purposes. see getLastMessage()
         */
        this.lastMessage = message;

        //@author A0112092W
        System.out.println(message);
    }

    /**
     * This method is used to output the task for the user in certain format.
     *
     * @param taskList
     * @throws OutputExecuteException
     */
    public void outputTask(Task[] taskList) throws OutputExecuteException {
        String[][] table = this.initTable(taskList.length + 1,
                MagicNumbersAndConstants.NUMBER_TASK_PROPERTIES);
        int iteration = taskList.length + 1;
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        table = this.initNullTaskTable(table, 1);

        for (int i = 1; i < iteration; i++) {
            // task should be not null
            if (taskList[i - 1] == null) {
                throw new OutputExecuteException(
                        OuputExecuteMessage.EXCEPTION_NULL_TASK);
            }

            if (taskList[i - 1].getTitle() == null) {
                table[i][0] = "-";
            } else {
                table[i][0] = taskList[i - 1].getTitle();
            }

            if (taskList[i - 1].getCategory() == null) {
                table[i][1] = "-";
            } else {
                table[i][1] = taskList[i - 1].getCategory();
            }
            if (taskList[i - 1].getDescription() == null) {
                table[i][2] = "-";
            } else {
                table[i][2] = taskList[i - 1].getDescription();
            }
            if (taskList[i - 1].getStartDate() == null) {
                table[i][3] = "-";
            } else {
                table[i][3] = df.format(taskList[i - 1].getStartDate());
            }
            if (taskList[i - 1].getEndDate() == null) {
                table[i][4] = "-";
            } else {
                table[i][4] = df.format(taskList[i - 1].getEndDate());
            }
            if (taskList[i - 1].getLocation() == null) {
                table[i][5] = "-";
            } else {
                table[i][5] = taskList[i - 1].getLocation();
            }

            table[i][6] = String.valueOf(taskList[i - 1].getPriority());

            if (taskList[i - 1].getReminder() == null) {
                table[i][7] = "-";
            } else {
                table[i][7] = df.format(taskList[i - 1].getReminder());
            }

            table[i][8] = taskList[i - 1].getTaskType().toString();

            if (taskList[i - 1].getStatus() == true) {
                table[i][9] = "Done";
            } else {
                table[i][9] = "Not Done Yet";
            }
        }
        printTable(table);
    }

    private String[][] initNullTaskTable(String[][] table, int taskNumber) {
        int i = taskNumber;
        table[i][0] = "-";

        table[i][1] = "-";

        table[i][2] = "-";

        table[i][3] = "-";

        table[i][4] = "-";

        table[i][5] = "-";

        table[i][6] = "-";

        table[i][7] = "-";

        table[i][8] = "-";

        table[i][9] = "-";
        return table;
    }

    // initialize the table with adding the first row for each of the task's
// properties
    private String[][] initTable(int taskNumber, int taskProperties) {
        String[][] table = new String[taskNumber][taskProperties];
        table[0][0] = "Title";
        table[0][1] = "Category";
        table[0][2] = "Description";
        table[0][3] = "StartDate";
        table[0][4] = "EndDate";
        table[0][5] = "Location";
        table[0][6] = "Priority";
        table[0][7] = "Reminder";
        table[0][8] = "TaskType";
        table[0][9] = "Status";

        return table;
    }

    @Override
    public void updateUI(Response res) {
        if (res.reply != null) {
            outputMessage(res.reply);
        }
        if (res.taskList != null) {
            try {
                outputTask(res.taskList);
            } catch (OutputExecuteException e) {
                e.printStackTrace();
            }
        }

    }

}
