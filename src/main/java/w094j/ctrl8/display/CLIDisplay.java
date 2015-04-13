package w094j.ctrl8.display;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.database.config.CLIDisplayConfig;
import w094j.ctrl8.exception.OutputExecuteException;
import w094j.ctrl8.message.MagicNumbersAndConstants;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.message.OuputExecuteMessage;
import w094j.ctrl8.parse.statement.Statement;
import w094j.ctrl8.pojo.Actions;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.pojo.Task.TaskType;

import com.google.gson.Gson;

/**
 * Class implements Display Interface as a simple CLI
 */

// @author A0112092W
public class CLIDisplay extends Display {

    private static final String BETWEEN_SEPERATOR = " | ";

    private static final String DEADLINED_TASK_NAME = "Tasks with Deadline";

    private static final String END_SEPERATOR = " |";
    private static final String FLOATING_TASK_NAME = "Tasks";
    private static final char LINE_COMPONENT = '-';
    private static Logger logger = LoggerFactory.getLogger(CLIDisplay.class);
    private static final String START_SEPERATOR = "| ";
    private static final String TIMED_TASK_NAME = "Events";
    private static final char TITLE_LINE_COMPONENT = '=';
    private CLIDisplayConfig config;

    // @author A0110787A
    private String lastMessage;

    // @author A0112092W
    /**
     * Public constructor for a CLI Display
     */
    CLIDisplay(CLIDisplayConfig cliDisplayConfig) {
        this.config = cliDisplayConfig;
    }

    /**
     * Returns n number of lineComponent.
     *
     * @param n
     * @param lineComponent
     */
    private static String getDashes(int n, Character lineComponent) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < n; x++) {
            sb.append(lineComponent);
        }
        return sb.toString();
    }

    /**
     * Print n number of lineComponent
     *
     * @param n
     * @param lineComponent
     */
    private static void printDashes(int n, Character lineComponent) {
        System.out.println(getDashes(n, lineComponent));
    }

    /**
     * This method is used to print a table with format of following the format
     * of right justified table x xxx yyy y zz zz
     *
     * @param title
     * @param table
     */
    private static void printTable(String title, String[][] table) {

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

        // print the title bar
        int boundaryOffset = (START_SEPERATOR.length() + END_SEPERATOR.length());
        int sumLength = boundaryOffset - BETWEEN_SEPERATOR.length();
        for (int eaLength : lengths) {
            sumLength += eaLength + BETWEEN_SEPERATOR.length();
        }
        printDashes(sumLength, TITLE_LINE_COMPONENT);
        int leftOffset = (((sumLength + title.length()) / 2) - START_SEPERATOR
                .length());
        int rightOffset = sumLength - leftOffset - END_SEPERATOR.length();
        System.out.println(START_SEPERATOR
                + String.format("%" + leftOffset + "s%" + rightOffset + "s",
                        title, END_SEPERATOR));
        printDashes(sumLength, TITLE_LINE_COMPONENT);

        // Generate a format string for each column
        String[] formats = new String[lengths.length];
        for (int i = 0; i < lengths.length; i++) {
            formats[i] = "%1$"
                    + lengths[i]
                            + "s"
                            + ((i + 1) == lengths.length ? END_SEPERATOR + "\n"
                                    : BETWEEN_SEPERATOR);
        }

        // Print 'em out
        StringBuilder sb = new StringBuilder();
        try (Formatter formatter = new Formatter(sb)) {

            for (int x = 0; x < table.length; x++) {
                String[] element = table[x];
                sb.append(START_SEPERATOR);
                for (int j = 0; j < element.length; j++) {
                    formatter.format(formats[j], element[j]);
                }
                if (x == 0) {
                    sb.append(getDashes(sumLength, LINE_COMPONENT));
                    sb.append("\n");
                }
            }
            System.out.print(sb.toString());
            printDashes(sumLength, LINE_COMPONENT);
        }
    }

    // @author A0112092W
    @Override
    public InputStream getInputStream() {
        return System.in;
    }

    // @author A0110787A
    /**
     * For testing purposes. Facilitates JUnit testing of individual modules
     * using CLIDisplay
     *
     * @return lastMessage
     */
    public String getLastMessage() {
        return this.lastMessage;

    }

    @Override
    public void goodbye() {
        // TODO Not Implemented Yet

    }

    // @author A0110787A
    /**
     * For testing purposes. see getLastMessage()
     *
     * @param message
     */
    public void outputMessage(String message) {

        this.lastMessage = message;

        // @author A0112092W
        System.out.println(message);
    }

    /**
     * This method is used to output the task for the user in certain format.
     *
     * @param title
     * @param taskList
     * @param taskType
     * @throws OutputExecuteException
     */
    public void outputTask(String title, Task[] taskList, TaskType taskType)
            throws OutputExecuteException {

        System.out.println();

        boolean[] isActiveColumn = new boolean[MagicNumbersAndConstants.NUMBER_TASK_PROPERTIES];

        for (int x = 0; x < MagicNumbersAndConstants.NUMBER_TASK_PROPERTIES; x++) {

            isActiveColumn[x] = false;

            for (Task eaTask : taskList) {
                switch (x) {
                    case 0 :
                        if (eaTask.getTitle() != null) {
                            isActiveColumn[x] = true;
                            break;
                        }
                        break;
                    case 1 :
                        if (eaTask.getCategory() != null) {
                            isActiveColumn[x] = true;
                            break;
                        }
                        break;
                    case 2 :
                        if (eaTask.getDescription() != null) {
                            isActiveColumn[x] = true;
                            break;
                        }
                        break;
                    case 3 :
                        if (eaTask.getStartDate() != null) {
                            isActiveColumn[x] = true;
                            break;
                        }
                        break;
                    case 4 :
                        if (eaTask.getEndDate() != null) {
                            isActiveColumn[x] = true;
                            break;
                        }
                        break;
                    case 5 :
                        if (eaTask.getLocation() != null) {
                            isActiveColumn[x] = true;
                            break;
                        }
                        break;
                    case 6 :
                        if (eaTask.getPriority() != null) {
                            isActiveColumn[x] = true;
                            break;
                        }
                        break;
                    case 7 :
                        if (eaTask.getReminder() != null) {
                            isActiveColumn[x] = true;
                            break;
                        }
                        break;
                    case 8 :
                        if (eaTask.getStatus() != null) {
                            isActiveColumn[x] = true;
                            break;
                        }
                        break;
                }
            }

        }

        int activeColumnSize = 0;
        for (boolean element : isActiveColumn) {
            if (element) {
                activeColumnSize++;
            }
        }

        String[][] table = this.initTable(taskList.length + 1, isActiveColumn,
                activeColumnSize, taskType);
        int iteration = taskList.length + 1;
        DateFormat df = new SimpleDateFormat(this.config.getDateFormat());
        table = this.initNullTaskTable(table, 1, activeColumnSize);

        for (int i = 1; i < iteration; i++) {
            // task should be not null
            if (taskList[i - 1] == null) {
                throw new OutputExecuteException(
                        OuputExecuteMessage.EXCEPTION_NULL_TASK);
            }

            int index = 0;
            for (int x = 0; x < isActiveColumn.length; x++) {
                if (isActiveColumn[x]) {
                    switch (x) {
                        case 0 :
                            if (taskList[i - 1].getTitle() == null) {
                                table[i][index] = "-";
                            } else {
                                table[i][index] = taskList[i - 1].getTitle();
                            }
                            break;
                        case 1 :
                            if (taskList[i - 1].getCategory() == null) {
                                table[i][index] = "-";
                            } else {
                                table[i][index] = taskList[i - 1].getCategory();
                            }
                            break;
                        case 2 :
                            if (taskList[i - 1].getDescription() == null) {
                                table[i][index] = "-";
                            } else {
                                table[i][index] = taskList[i - 1]
                                        .getDescription();
                            }
                            break;
                        case 3 :
                            if (taskList[i - 1].getStartDate() == null) {
                                table[i][index] = "-";
                            } else {
                                table[i][index] = df.format(taskList[i - 1]
                                        .getStartDate());
                            }
                            break;
                        case 4 :
                            if (taskList[i - 1].getEndDate() == null) {
                                table[i][index] = "-";
                            } else {
                                table[i][index] = df.format(taskList[i - 1]
                                        .getEndDate());
                            }
                            break;
                        case 5 :
                            if (taskList[i - 1].getLocation() == null) {
                                table[i][index] = "-";
                            } else {
                                table[i][index] = taskList[i - 1].getLocation();
                            }
                            break;
                        case 6 :
                            if (taskList[i - 1].getPriority() == null) {
                                table[i][index] = "-";
                            } else {
                                table[i][index] = String
                                        .valueOf(taskList[i - 1].getPriority());
                            }
                            break;
                        case 7 :
                            if (taskList[i - 1].getReminder() == null) {
                                table[i][index] = "-";
                            } else {
                                table[i][index] = df.format(taskList[i - 1]
                                        .getReminder());
                            }
                            break;
                        case 8 :
                            if (taskList[i - 1].getStatus()) {
                                table[i][index] = "Yes";
                            } else {
                                table[i][index] = "-";
                            }
                            break;
                    }
                    index++;
                }
            }

        }
        printTable(title, table);
    }

    @Override
    public <T> T promptUser(Prompt<T> prompt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateUI(Response res) {

        logger.debug("Updating Response(" + new Gson().toJson(res) + ")");

        if (res.getCommandRan() == null) {
            System.out.println(res.getException().getMessage());
        } else {

            if (res.reply != null) {
                this.outputMessage(res.reply);
            }
            if (res.taskList != null) {

                List<Task> floatingTasks = new ArrayList<>();
                List<Task> timedTasks = new ArrayList<>();
                List<Task> deadlinedTasks = new ArrayList<>();

                for (Task eaTask : res.taskList) {
                    switch (eaTask.getTaskType()) {
                        case DEADLINE :
                            deadlinedTasks.add(eaTask);
                            break;
                        case FLOATING :
                            floatingTasks.add(eaTask);
                            break;
                        case TIMED :
                            timedTasks.add(eaTask);
                            break;
                        default :
                            // Should never be an incomplete task
                            assert (false);
                            break;
                    }
                }

                try {
                    if (floatingTasks.size() > 0) {
                        this.outputTask(FLOATING_TASK_NAME, floatingTasks
                                .toArray(new Task[floatingTasks.size()]),
                                TaskType.FLOATING);
                        System.out.println();
                    }
                    if (deadlinedTasks.size() > 0) {
                        this.outputTask(DEADLINED_TASK_NAME, deadlinedTasks
                                .toArray(new Task[deadlinedTasks.size()]),
                                TaskType.DEADLINE);
                        System.out.println();
                    }
                    if (timedTasks.size() > 0) {
                        this.outputTask(
                                TIMED_TASK_NAME,
                                timedTasks.toArray(new Task[timedTasks.size()]),
                                TaskType.TIMED);
                    }
                } catch (OutputExecuteException e) {
                    e.printStackTrace();
                }

            }

            if (res.alias != null) {
                this.outputAliases(res.alias);
            }
            if (res.actions != null) {
                this.outputActions(res.actions);
            }
        }

        if (!((res.getCommandRan() != null) && !res.isContinueExecution())) {

            System.out.print(String.format(this.config.getPromptDefault(),
                    this.config.getAppName()));
        }

    }

    @Override
    public void welcome() {
        System.out.println(String.format(this.config.getWelcomeMessage(),
                this.config.getAppName()));
        System.out.print(String.format(this.config.getPromptDefault(),
                this.config.getAppName()));
    }

    private String[][] initNullTaskTable(String[][] table, int taskNumber,
            int activeColumnSize) {
        int i = taskNumber;

        for (int x = 0; x < activeColumnSize; x++) {
            table[i][x] = "-";
        }

        return table;
    }

    // initialize the table with adding the first row for each of the task's
// properties
    private String[][] initTable(int taskNumber, boolean[] isActiveColumn,
            int activeColumnSize, TaskType taskType) {
        String[][] table = new String[taskNumber][activeColumnSize];
        int index = 0;
        for (int x = 0; x < isActiveColumn.length; x++) {
            if (isActiveColumn[x]) {
                switch (x) {
                    case 0 :
                        table[0][index] = "Title";
                        index++;
                        break;
                    case 1 :
                        table[0][index] = "Category";
                        index++;
                        break;
                    case 2 :
                        table[0][index] = "Description";
                        index++;
                        break;
                    case 3 :
                        table[0][index] = "From";
                        index++;
                        break;
                    case 4 :
                        if (taskType.equals(TaskType.DEADLINE)) {
                            table[0][index] = "Due";
                        } else {
                            table[0][index] = "To";
                        }
                        index++;
                        break;
                    case 5 :
                        table[0][index] = "Location";
                        index++;
                        break;
                    case 6 :
                        table[0][index] = "Priority";
                        index++;
                        break;
                    case 7 :
                        table[0][index] = "Reminder";
                        index++;
                        break;
                    case 8 :
                        table[0][index] = "Done";
                        index++;
                        break;
                }
            }
        }

        return table;
    }

    /**
     * Output the actions to user in appropiate way
     *
     * @param actions
     */
    private void outputActions(ArrayList<Actions> actions) {
        if (actions.size() == 0) {
            System.out.println("No actions found");
        }
        for (int i = 0; i < actions.size(); i++) {
            Statement statement = actions.get(i).getStatement();
            System.out.print(i + 1 + ". Command:");
            System.out.print(statement.getCommand().toString());
            System.out.print(" String:");
            System.out.println(statement.getStatementArgumentsOnly());
        }

    }

    /**
     * Output the alias data to user in appropiate way
     *
     * @param alias
     */
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

}
