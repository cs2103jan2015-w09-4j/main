package w094j.ctrl8.application.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.application.GUICore;

public class ConsoleSceneController {
    private static final String __newline = "\n";

    private String displayBuffer; // Buffer for the display
    private InputStream inStream;

    private Logger logger = LoggerFactory
            .getLogger(ConsoleSceneController.class);
    private GUICore root; // Pointer back to the root

    @FXML
    private TextArea textDisplay;
    @FXML
    private TextField textInput;

    public ConsoleSceneController() {
    }

    /**
     * Appends a string to the displayBuffer then updates the textDisplay
     * 
     * @param input
     *            string to append
     */
    public void appendToDisplay(String input) {
        this.displayBuffer += input + __newline;
        this.textDisplay.appendText(input + __newline);
    }

    public InputStream getInputStream() {
        return this.inStream;
    }

    /**
     * Called when the user presses the "Enter" key on his keyboard while the
     * ConsoleScene is up. Reads the text the user has typed into the TextField
     * textInput, as a String and passes it to the terminal for interpreting
     * instructions.
     */
    public void onEnter() {
        String input = this.textInput.getText();

        this.logger.debug("Received user string: " + input);

        this.inStream = new ByteArrayInputStream(input.getBytes());

        // @Deprecated because the main role is simply to put it on the stream
        /*
         * try { Statement.parse(input).execute(this.root.terminal); } catch
         * (InvalidParameterException e) {
         * this.logger.debug("InvalidParameterException thrown");
         * appendToDisplay(e.getMessage()); } catch (CommandExecuteException e)
         * { this.logger.debug("CommandExecuteException thrown");
         * appendToDisplay(e.getMessage()); } catch (ParameterParseException e)
         * { this.logger.debug("ParameterParseException thrown");
         * appendToDisplay(e.getMessage()); } appendToDisplay(input);
         * this.lastTextInput = this.textInput.toString();
         * this.textInput.setText(""); // clears the buffer
         */
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param GUICore
     */
    public void setRoot(GUICore root) {
        this.root = root;

        // Get the root to provide the initial text display
        this.displayBuffer = this.root.getConsoleInitString();

        // Update displayed text
        this.textDisplay.setText(new String()); // Flushes the display
        this.textDisplay.appendText(displayBuffer); // Activates listener
    }

    @FXML
    private void initialize() {
        // Creates an InputStream that is initially empty
        this.inStream = new ByteArrayInputStream(new String().getBytes());

        // Initialise text display
        this.textDisplay.setStyle("-fx-text-fill: black; -fx-font-size: 12;"); /*
                                                                                * Black
                                                                                * text
                                                                                * size
                                                                                * 12
                                                                                */
        this.textDisplay.setWrapText(true); // wraps display

        /*
         * Add a listener that auto-scrolls the display to the bottom whenever
         * there are new strings appended
         */
        this.textDisplay.textProperty().addListener(
                new ChangeListener<Object>() {
                    @Override
                    public void changed(ObservableValue<?> observable,
                            Object oldValue, Object newValue) {
                        textDisplay.setScrollTop(Double.MAX_VALUE);
                    }
                });

        // Initialise buffer to be empty string
        this.displayBuffer = "";
        this.textDisplay.appendText(this.displayBuffer);

        // Initialise text input
        this.textInput.setStyle("-fx-text-fill: black; -fx-font-size: 20;"); /*
                                                                              * Black
                                                                              * text
                                                                              * size
                                                                              * 20
                                                                              */
        this.textInput.setAlignment(Pos.TOP_LEFT); // Align top left
    }

}
