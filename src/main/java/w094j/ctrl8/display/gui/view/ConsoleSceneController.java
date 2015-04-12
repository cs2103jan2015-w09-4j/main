//@author A0110787A
package w094j.ctrl8.display.gui.view;

import java.io.InputStream;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.DisplayControllerConfig;
import w094j.ctrl8.database.config.GUITextDisplayConfig;
import w094j.ctrl8.database.config.GUITextInputConfig;
import w094j.ctrl8.display.gui.GUICore;
import w094j.ctrl8.display.gui.model.FXTextFieldInputStream;

/**
 * Controller class for the JavaFX Application. Manages capturing input from
 * Application window as well as updating its view.
 */
public class ConsoleSceneController {
    private static final String __newline = "\n";
    private static final String CSS_FAINT_FOCUS_TRANSPARENT = "-fx-faint-focus-color: transparent;";
    private static final String CSS_FOCUS_TRANSPARENT = "-fx-focus-color: transparent;";
    private static final String CSS_FONT_SIZE = "-fx-font-size: %1$2s ;";
    private static final String CSS_TEXT_FILL = "-fx-text-fill: %1$2s ;";

    public byte[] input;
    private String displayBuffer; // Buffer for the display
    private FXTextFieldInputStream inputStream;

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

    /**
     * Applies a DisplayControllerConfig to modify certain settings. Refer to
     * DisplayControllerConfig class for more details.
     * 
     * @param controllerConfig
     */
    public void applyConfig(DisplayControllerConfig controllerConfig) {
        applyToTextDisplay(controllerConfig.getGUITextDisplayConfig());
        applyToTextInput(controllerConfig.getGUITextInputConfig());

    }

    public InputStream getInputStream() {
        return this.inputStream;
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

    private void applyToTextDisplay(GUITextDisplayConfig textDisplayConfig) {
        this.textDisplay.setStyle(String.format(CSS_FONT_SIZE,
                textDisplayConfig.getTextSize()));
        this.textDisplay.setStyle(String.format(CSS_TEXT_FILL,
                textDisplayConfig.getTextColour()));

    }

    private void applyToTextInput(GUITextInputConfig textInputConfig) {
        this.textInput.setStyle(String.format(CSS_FONT_SIZE,
                textInputConfig.getTextSize()));
        this.textInput.setStyle(String.format(CSS_TEXT_FILL,
                textInputConfig.getTextColour()));

    }

    @FXML
    private void initialize() {
        // Initialise text display
        this.textDisplay.setWrapText(false); // disable wrapping
        this.textDisplay.setEditable(false); // Disables editing
        this.textDisplay.setStyle(CSS_FOCUS_TRANSPARENT);
        this.textDisplay.setStyle(CSS_FAINT_FOCUS_TRANSPARENT);

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
        this.textInput.setAlignment(Pos.TOP_LEFT); // Align top left
        this.textInput.setStyle(CSS_FOCUS_TRANSPARENT);
        this.textInput.setStyle(CSS_FAINT_FOCUS_TRANSPARENT);

        /*
         * Create an InputStream that specifically captures input from the
         * TextArea. A listener notifies the inputstream to unblock itself once
         * enter button is pressed
         */
        this.inputStream = new FXTextFieldInputStream(textInput);
        this.logger.info("InputStream Thread created, Init complete");
    }
}
