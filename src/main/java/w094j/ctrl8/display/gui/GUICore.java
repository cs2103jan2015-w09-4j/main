//@author A0110787A
package w094j.ctrl8.display.gui;

import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.GUIDisplayConfig;
import w094j.ctrl8.display.gui.view.ConsoleSceneController;
import w094j.ctrl8.display.gui.view.LocalResource;
import w094j.ctrl8.message.NormalMessage;

/**
 * The application core of the GUI implementation. To create an application
 * window, simply initialise the object using its constructors.
 *
 * <pre>
 * E.g
 * new GUICore(new String[] {""});
 *
 * Refer to
 * http://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html
 * </pre>
 */
public class GUICore extends Application implements Runnable {

    private static final String __newline = "\n";
// TODO
    private static ConsoleSceneController consoleController;
    private static Logger logger = LoggerFactory.getLogger(GUICore.class);
    private GUIDisplayConfig config;
    private Stage primaryStage; // Default stage
    private BorderPane rootLayout; // Wrapper for internal components
    private Thread main;

    public GUICore() {
        /*
         * To be left empty. Because Application.launch invokes the default
         * constructor, but we use the loaded constructor to initialise the
         * application
         */
    }

    public GUICore(GUIDisplayConfig config, Thread main) {
        this.config = config;
        this.main = main;
    }

    /**
     * @return the consoleController
     */
    public ConsoleSceneController getConsoleController() {
        System.out.println(this + ":" + this.consoleController);
        return this.consoleController;
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
        return this.getConsoleController().getInputStream();
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
    public void run() {
        launch(this.config.appArgs);
    }

    /**
     * @param consoleController
     *            the consoleController to set
     */
    public void setConsoleController(ConsoleSceneController consoleController) {
        this.consoleController = consoleController;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(NormalMessage.APP_NAME);

        this.initRootLayout();

        this.showConsole();
        
        synchronized (this) {
            this.notify();
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
                    .getResource(LocalResource.RootLayout));
            this.rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout
            Scene rootLayoutScene = new Scene(this.rootLayout);
            this.primaryStage.setScene(rootLayoutScene);
            this.primaryStage.setMinHeight(438); // trial and error numbers
            this.primaryStage.setMinWidth(516); // trial and error numbers
            this.primaryStage.setResizable(true); /*
             * Enable resizing the window
             */
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
            BorderPane consoleScene = (BorderPane) loader.load();

            // TODO
            consoleScene
            .setStyle("-fx-faint-focus-color: transparent;-fx-focus-color: transparent;");

            // Place console scene in center pane of the rootLayout
            this.rootLayout.setCenter(consoleScene);

            // Give controller access
            this.setConsoleController(loader.getController());
            this.getConsoleController().setRoot(this);
            System.out.println(this.getConsoleController());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
