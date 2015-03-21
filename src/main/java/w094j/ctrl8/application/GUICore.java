package w094j.ctrl8.application;

import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.application.view.ConsoleSceneController;
import w094j.ctrl8.application.view.LocalResource;
import w094j.ctrl8.message.NormalMessage;

//@author A0110787A
/**
 * The application core of the GUI implementation. To create an application
 * window, simply initialise the object using its constructors.
 *
 * <pre>
 * E.g
 * new GUICore();
 * 
 * Refer to
 * http://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html
 * </pre>
 */
public class GUICore extends Application {

    private static final String __newline = "\n";
    private static Logger logger = LoggerFactory.getLogger(GUICore.class);
    public ConsoleSceneController consoleController;
    private Stage primaryStage; // Default stage
    private BorderPane rootLayout; // Wrapper for internal components

    public GUICore() {
// launch();
    }

    public GUICore(String[] args) {
        launch(args);
    }

    // TODO replace with factory calling the launch instead
    public static void main(String[] args) {
        new GUICore(args);
    }

    /**
     * Generates initial text to display in TextArea textDisplay.
     *
     * @return String
     */
    public String getConsoleInitString() {
        return NormalMessage.WELCOME_MESSAGE + __newline;
    }

    public InputStream getInputStream() {
        return this.consoleController.getInputStream();
    }

    /**
     * Returns the main stage. Main purpose is to allow child-scenes to be able
     * to link back.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    @Override
    public void init() throws Exception {
        logger.debug("Initialising GUICore...");
        super.init();

        this.getParameters().getRaw();

        logger.debug("GUICore initialised!");
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(NormalMessage.APP_NAME);

        this.initRootLayout();

        this.showConsole();
    }

    /**
     * Initialises the rootlayout
     */
    private void initRootLayout() {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUICore.class
                    .getResource(LocalResource.RootLayout));
            this.rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout
            Scene rootLayoutScene = new Scene(this.rootLayout);
            this.primaryStage.setScene(rootLayoutScene);
            this.primaryStage.setResizable(false); // Disable resizing the
            // window
            this.primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shows the console inside the root layout
     */
    private void showConsole() {
        try {
            // Load console scene
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUICore.class
                    .getResource(LocalResource.ConsoleScene));
            AnchorPane consoleScene = (AnchorPane) loader.load();

            // Place console scene in center pane of the rootLayout
            this.rootLayout.setCenter(consoleScene);

            // Give controller access
            this.consoleController = loader.getController();
            this.consoleController.setRoot(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
