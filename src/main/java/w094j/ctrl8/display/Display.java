package w094j.ctrl8.display;

import w094j.ctrl8.exception.OutputExecuteException;
import w094j.ctrl8.pojo.Task;

/**
 * This interface describes the possible interactions a Terminal object can with
 * a display UI. Any display method (CLI/GUI) should (minimally) implement this
 * interface
 */
/**
 * @author Han Liang Wee, Eric(A0065517A)
 */
public interface Display {
    /**
     * Extracts userinput from UI
     * 
     * @return user input as String object
     */
    public String getUserInput();

    /**
     * Informs the UI what message to display to the user
     * 
     * @param message
     *            String to display to the user
     */
    public void outputMessage(String message);

    /**
     * display the task to user
     * 
     * @param taskList
     * @throws OutputExecuteException
     */
    public void outputTask(Task[] taskList) throws OutputExecuteException;
}
