//@author A0110787A
package w094j.ctrl8.display;

import java.io.InputStream;

import w094j.ctrl8.pojo.Response;

/**
 * This interface describes the possible interactions a Terminal object can with
 * a display UI. Any display method (CLI/GUI) should (minimally) implement this
 * interface
 */
public interface IDisplay {
    /**
     * Gets the inputstream of the Display object. The primary method of getting
     * input from user. Stream prevents blocking from occurring during the
     * period after user inputs and before a proper output is produced.
     *
     * @return InputStream user input as an inputstream.
     */
    public InputStream getInputStream();

    /**
     * Informs the UI to update itself with the response object provided.
     * 
     * @param res
     *            The response POJO which lists all the variables that a
     *            response may contain.
     */
    public void updateUI(Response res);
}
