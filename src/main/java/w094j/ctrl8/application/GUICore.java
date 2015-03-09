package w094j.ctrl8.application;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.application.view.ConsoleSceneController;

//@author A0110787A
/**
 * The application core of the GUI implementation. To create an application
 * window, invoke the function: GUICore.launch(args), where args
 * 
 * <pre>
 * Refer to
 * http://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html
 * </pre>
 */
public class GUICore extends Application {

	private static final String __newline = "\n";
	private Stage primaryStage; // Default stage
	private BorderPane rootLayout; // Wrapper for internal fxml
	private ConsoleSceneController consoleController;
	private static Logger logger = LoggerFactory.getLogger(GUICore.class);

	@Override
	public void init() throws Exception {
		super.init();

		List<String> args = this.getParameters().getRaw();
		// TODO do something with the arguements
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Ctrl-8"); // TODO extract magic string

		initRootLayout();

		showConsole();
	}

	/**
	 * Shows the console inside the root layout
	 */
	private void showConsole() {
		try {
			// Load console scene
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUICore.class
					.getResource("view/ConsoleScene.fxml"));
			AnchorPane consoleScene = (AnchorPane) loader.load();

			// Set console anchored to top of root layout
			this.rootLayout.setCenter(consoleScene);

			// Give controller access
			this.consoleController = loader.getController();
			this.consoleController.setRoot(this);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Initialises the rootlayout
	 */
	private void initRootLayout() {
		try {
			// Load root layout from fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUICore.class
					.getResource("view/RootLayout.fxml"));
			this.rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout
			Scene rootLayoutScene = new Scene(rootLayout);
			this.primaryStage.setScene(rootLayoutScene);
			this.primaryStage.setResizable(false); // Disable resizing the
													// window
			this.primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Returns the main stage. Main purpose is to allow child-scenes to be able
	 * to link back.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	// TODO replace with factory calling the launch instead
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Generates initial text to display in TextArea textDisplay.
	 * 
	 * @return String
	 */
	public String getConsoleInitString() {
		String welcomeMessage = "Ctrl-8"
				+ __newline
				+ "Welcome to Ctrl-8. Below are the options you can choose"
				+ __newline
				+ "<TBC>"
				+ __newline
				+ "For more information please refer to https://github.com/cs2103jan2015-w09-4j/main"
				+ __newline;
		return welcomeMessage;
	}
}
