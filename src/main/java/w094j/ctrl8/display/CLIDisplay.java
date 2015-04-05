package w094j.ctrl8.display;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.database.config.CLIDisplayConfig;
import w094j.ctrl8.exception.OutputExecuteException;
import w094j.ctrl8.message.ErrorMessage;
import w094j.ctrl8.message.MagicNumbersAndConstants;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.message.OuputExecuteMessage;
import w094j.ctrl8.pojo.HistoryData;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.Statement;

/**
 * Class implements Display Interface as a simple CLI How to use: To get
 * userinput as a String, call CLIDisplay.getUserInput() To display an output,
 * call CLIDisplay.outputMessage(message)
 */

// @author A0112092W
public class CLIDisplay extends Display {
    private static CLIDisplay instance;
    private static Logger logger = LoggerFactory.getLogger(CLIDisplay.class);
    private BufferedReader br;
    private InputStream inStream;

    // @author A0110787A
    private String lastMessage;

    // @author A0112092W
    /**
     * Public constructor for a CLI Display
     */
    CLIDisplay(CLIDisplayConfig cliDisplayConfig) {
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

    // @author A0112092W
    @Override
    public InputStream getInputStream() {

        this.inStream = new ByteArrayInputStream(this.getUserInput().getBytes(
                StandardCharsets.UTF_8));
        return this.inStream;
    }

    // @author A0110787A
    /*
     * For testing purposes. Facilitates JUnit testing of individual modules
     * using CLIDisplay
     */
    public String getLastMessage() {
        return this.lastMessage;

    }

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
            if (taskList[i - 1].getPriority() == null) {
                table[i][6] = "-";
            } else {
                table[i][6] = String.valueOf(taskList[i - 1].getPriority());
            }
            if (taskList[i - 1].getReminder() == null) {
                table[i][7] = "-";
            } else {
                table[i][7] = df.format(taskList[i - 1].getReminder());
            }

            table[i][8] = taskList[i - 1].getTaskType().toString();

            if (taskList[i - 1].getStatus()) {
                table[i][9] = "Done";
            } else {
                table[i][9] = "Not Done Yet";
            }
        }
        printTable(table);
    }

    @Override
    public void updateUI(Response res) {
        if (res.reply != null) {
            this.outputMessage(res.reply);
        }
        if (res.taskList != null) {
            try {
                this.outputTask(res.taskList);
            } catch (OutputExecuteException e) {
                e.printStackTrace();
            }

        }
        if (res.history != null) {
            this.outputHistory(res.history);
        }
        if (res.alias != null) {
            this.outputAliases(res.alias);
        }

    }

    // @author A0112092W
    // temporary function to get user's input before input stream is properly
// implemented.
    private String getUserInput() {
        String nextLine = null;
        try {
            nextLine = this.br.readLine();
        } catch (IOException e) {
            this.outputMessage(ErrorMessage.ERROR_READING_INPUT);
            return null;
        }
        return nextLine;
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

    private void outputAliases(AliasData alias) {
        Map<String, String> aliases = alias.getAliasMap();
        if (aliases.size() == 0) {
            System.out.println(NormalMessage.ALIAS_MAP_EMPTY);
        }
        for (int i = 0; i < aliases.size();) {
            for (String key : aliases.keySet()) {
                String value = aliases.get(key);
                System.out.print(i + 1 + ". Alias: " + key);
                System.out.println(" String: " + value);
                i++;
            }
        }
    }

    private void outputHistory(HistoryData history) {
        ArrayList<Statement> historyList = history.getHistoryList();

        for (int i = 0; i < historyList.size(); i++) {
            Statement statement = historyList.get(i);
            System.out.print(i + 1 + ". Command:");
            System.out.print(statement.getCommand().toString());
            System.out.print(" String:");
            System.out.println(statement.getStatementArgumentsOnly());
        }
    }

}
