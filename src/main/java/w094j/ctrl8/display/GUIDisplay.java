//@author A0110787A
package w094j.ctrl8.display;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.GUIDisplayConfig;
import w094j.ctrl8.display.gui.GUICore;
import w094j.ctrl8.exception.OutputExecuteException;
import w094j.ctrl8.message.MagicNumbersAndConstants;
import w094j.ctrl8.message.OuputExecuteMessage;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.pojo.Task;

/**
 * Class implements Display using JavaFX framework which creates an application
 * for the user to interact with. The application window runs on a seperate
 * thread to prevent the main thread from freezing up due to the nature of
 * JavaFX. Instead, thread notifications are used to notify the main thread (who
 * holds GUIDisplay) when it is initialised and ready for interaction.
 * 
 * <pre>
 * This implementation is incomplete and unstable.
 * </pre>
 */
@Deprecated
public class GUIDisplay extends Display {
    private GUICore guiCore;
    private Thread GUIThread;
    private Logger logger = LoggerFactory.getLogger(GUIDisplay.class);

    public GUIDisplay() {
        this.guiCore = new GUICore(new GUIDisplayConfig(),
                Thread.currentThread());
    }

    public GUIDisplay(GUIDisplayConfig config) {
        if ((config == null) || !config.isValid()) {
            this.logger
                    .debug("Invalid or null config received! Reverting to defaults.");
            this.guiCore = new GUICore(new GUIDisplayConfig(),
                    Thread.currentThread());
        } else {
            this.guiCore = new GUICore(config, Thread.currentThread());
        }
        this.GUIThread = new Thread(this.guiCore);
        /*
         * Ensure that GUIThread is completely initialised before returning
         * constructor
         */
        synchronized (this.GUIThread) {
            this.GUIThread.start();
        }
    }

    @Override
    public InputStream getInputStream() {
        return this.guiCore.getInputStream();
    }

    /*
     * XXX Buggy. Ideally the updateUI Function needs to notify the application
     * thread to update the UI while providing a shareable version of response
     * for the application thread to access. Current version does not work
     * because the main thread is tampering with JavaFX objects which throws
     * exceptions
     */
    @Override
    public void updateUI(Response res) {
        boolean allNull = true; // Initial assumption
        if (res.reply != null) {
            try {
                Thread.sleep(3000); // 1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            System.out.println(this.guiCore.getConsoleController());
            this.guiCore.getConsoleController().appendToDisplay(res.reply);
            allNull = false;
        }
        if (res.taskList != null) {
            try {
                this.outputTask(res.taskList);
            } catch (OutputExecuteException e) {
                this.logger.debug(e.getMessage());
            }
            allNull = false;
        }

        if (allNull) {
            this.logger
                    .debug("Respose object does not contain any useful information");
        }
    }

    // @author A0110787-reused
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

    /*
     * Initialize the table with adding the first row for each of the task's
     * properties.
     */
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

    /**
     * This method is used to output the task for the user in certain format.
     * Modified from CLIDisplay and contextualised for GUI
     *
     * @param taskList
     * @throws OutputExecuteException
     */
    private void outputTask(Task[] taskList) throws OutputExecuteException {
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
        this.printTable(table);
    }

    /**
     * This method is used to print a table with format of following the format
     * of right justified table x xxx yyy y zz zz
     *
     * <pre>
     * Modified from CLIDisplay and contextualised for GUI
     * </pre>
     *
     * @param table
     */
    private void printTable(String[][] table) {
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
        StringBuilder sb = new StringBuilder();
        for (String[] element : table) {
            for (int j = 0; j < element.length; j++) {
                sb.append(String.format(formats[j], element[j]));
            }
        }
        this.guiCore.getConsoleController().appendToDisplay(sb.toString());
    }

}
