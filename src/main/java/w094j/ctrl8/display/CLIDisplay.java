package w094j.ctrl8.display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import w094j.ctrl8.message.ErrorMessage;
import w094j.ctrl8.message.MagicNumbersAndConstants;
import w094j.ctrl8.pojo.Task;

/**
 * Class implements Display Interface as a simple CLI How to use: To get
 * userinput as a String, call CLIDisplay.getUserInput() To display an output,
 * call CLIDisplay.outputMessage(message)
 */

// @author A0112092W
public class CLIDisplay implements Display {
    private BufferedReader br;

    // @author A0110787A
    private String lastMessage;

    // @author A0112092W
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

    // @author A0110787A
    /*
     * For testing purposes. Facilitates JUnit testing of individual modules
     * using CLIDisplay
     */
    public String getLastMessage() {
        return this.lastMessage;

    }

    // @author A0112092W
    @Override
    public String getUserInput() {
        String nextLine = null;
        try {
            nextLine = this.br.readLine();
        } catch (IOException e) {
            this.outputMessage(ErrorMessage.ERROR_READING_INPUT);
            return null;
        }
        return nextLine;
    }

    @Override
    public void outputMessage(String message) {
        // @author A0110787A
        /*
         * For testing purposes. see getLastMessage()
         */
        this.lastMessage = message;

        // @author A0112092W
        System.out.println(message);
    }

    /**
     * This method is used to output the task for the user in certain format.
     *
     * @param taskList
     */
    @Override
    public void outputTask(Task[] taskList) {
        String[][] table = this.initTable(taskList.length + 1,
                MagicNumbersAndConstants.NUMBER_TASK_PROPERTIES);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        for (int i = 1; i < (taskList.length + 1); i++) {
            table[i][0] = taskList[i - 1].getTitle();
            table[i][1] = taskList[i - 1].getCategory();
            table[i][2] = taskList[i - 1].getDescription();
            table[i][3] = df.format(taskList[i - 1].getStartDate());
            table[i][4] = df.format(taskList[i - 1].getEndDate());
            table[i][5] = taskList[i - 1].getLocation();
            table[i][6] = String.valueOf(taskList[i - 1].getPriority());
            table[i][7] = df.format(taskList[i - 1].getReminder());
            table[i][8] = taskList[i - 1].getTaskType().toString();
            if (taskList[i - 1].getStatus() == true) {
                table[i][9] = "Done";
            } else {
                table[i][9] = "Not Done Yet";
            }
        }
        printTable(table);

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

}
