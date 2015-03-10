package w094j.ctrl8.application.view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.application.GUICore;

public class ConsoleSceneController {
	private static final String __newline = "\n";

	@FXML
	private TextArea textDisplay;

	@FXML
	private TextField textInput;

	private GUICore root; // Pointer back to the root
	private String displayBuffer; // Buffer for the display
	private static Logger logger = LoggerFactory
			.getLogger(ConsoleSceneController.class);

	public ConsoleSceneController() {
	}

	@FXML
	private void initialize() {
		// Initialise buffer to be empty string
		this.displayBuffer = "";

		// Initialise text display
		this.textDisplay.setStyle("-fx-text-fill: black; -fx-font-size: 14;"); /*
																				 * Black
																				 * text
																				 * size
																				 * 14
																				 */
		this.textDisplay.setWrapText(true); // wraps display
		this.textDisplay.setText(this.displayBuffer);

		// Initialise text input
		this.textInput.setStyle("-fx-text-fill: black; -fx-font-size: 20;"); /*
																			 * Black
																			 * text
																			 * size
																			 * 20
																			 */
		this.textInput.setAlignment(Pos.TOP_LEFT); // Align top left
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
		this.textDisplay.setText(displayBuffer);
	}

	/**
	 * Called when the user presses the "Enter" key on his keyboard while the
	 * ConsoleScene is up. Reads the text the user has typed into the TextField
	 * textInput, as a String and passes it to the terminal for interpreting
	 * instructions.
	 * 
	 */
	public void onEnter() {
		String input = this.textInput.getText();

		this.logger.debug("Received user string: " + input);

		// TODO link to Display Interface

		this.displayBuffer += input + __newline;
		this.textDisplay.setText(displayBuffer);
		this.textInput.setText(""); // clears the buffer
	}

}
