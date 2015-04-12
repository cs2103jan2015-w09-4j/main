package w094j.ctrl8.display;

import java.io.InputStream;

import w094j.ctrl8.database.config.DisplayConfig;
import w094j.ctrl8.pojo.Response;

/**
 * This interface describes the possible interactions a Terminal object can with
 * a display UI. Any display method (CLI/GUI) should (minimally) implement this
 * interface
 */
public abstract class Display {

    private static Display instance;

    /**
     * Gets the current instance of the CLIDisplay.
     *
     * @return the current instance.
     */
    public static Display getInstance() {
        if (instance == null) {
            throw new RuntimeException(
                    "Display must be initialized before retrieveing.");
        }
        return instance;
    }

    public static Display initInstance(DisplayConfig displayConfig) {

        if (instance != null) {
            throw new RuntimeException(
                    "Cannot initialize Display as it was initialized before.");
        } else {
            if (displayConfig.isGUI()) {
                instance = new GUIDisplay(displayConfig.getGUI());
            } else {
                instance = new CLIDisplay(displayConfig.getCLI());
            }
        }
        return instance;

    }

    /**
     * Gets the inputstream of the Display object. The primary method of getting
     * input from user. Stream prevents blocking from occurring during the
     * period after user inputs and before a proper output is produced.
     *
     * @return InputStream user input as an inputstream.
     */
    public abstract InputStream getInputStream();

    /**
     * Informs the UI to update itself with the response object provided.
     *
     * @param res
     *            The response POJO which lists all the variables that a
     *            response may contain.
     */
    public abstract void updateUI(Response res);

}
