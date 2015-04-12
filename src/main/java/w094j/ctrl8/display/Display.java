//@author A0110787A
package w094j.ctrl8.display;

import java.io.InputStream;

import w094j.ctrl8.database.config.DisplayConfig;
import w094j.ctrl8.pojo.Response;

/**
 * This abstract class defines the possible interactions a Terminal object use
 * to communicate with a Display Object. All Display classes should extend from
 * this abstract class to acheive the singleton pattern, avoiding multiple
 * instances of Display objects existing concurrently.
 */
public abstract class Display {

    private static Display instance;

    /**
     * Gets the current instance of the Display.
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

    /**
     * Initialises an instance of Display given a displayConfig
     * 
     * @param displayConfig
     * @return
     */
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
     * input from user.
     *
     * @return InputStream user input as an inputstream.
     */
    public abstract InputStream getInputStream();

    /**
     * Informs the UI to update itself with the response object provided. Refer
     * to w094j.ctrl8.pojo.Response.java for more information.
     * 
     * @param res
     *            The response POJO which lists all the variables that a
     *            response may contain.
     */
    public abstract void updateUI(Response res);

}
