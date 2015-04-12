package w094j.ctrl8.terminal;

import java.security.InvalidParameterException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.TerminalConfig;
import w094j.ctrl8.display.Display;
import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.parse.IParser;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112092W
public class Terminal {
    private static Terminal instance;
    private static Logger logger = LoggerFactory.getLogger(Terminal.class);
    private Display display;
    private IParser parser;
    private ITaskManager taskManager;

    private Terminal(TerminalConfig terminalConfig, ITaskManager taskManager,
            Display display, IParser parser) {
        assert (terminalConfig.isValid());

        this.taskManager = taskManager;
        this.display = display;
        this.parser = parser;
    }

    /**
     * Gets the current instance of the Terminal.
     *
     * @return the current instance.
     */
    public static Terminal getInstance() {
        if (instance == null) {
            throw new RuntimeException(
                    "Terminal must be initialized before retrieveing.");
        }

        return instance;
    }

    /**
     * Creates a Task Manager
     *
     * @return return the Task manager.
     */
    public static Terminal initInstance(TerminalConfig config,
            ITaskManager taskManager, Display display, IParser parser) {
        if (instance != null) {
            throw new RuntimeException(
                    "Cannot initialize Terminal as it was initialized before.");
        } else {
            // TO-DO put in config when config is done
            instance = new Terminal(config, taskManager, display, parser);
            logger.debug("terminal is initialized");
        }
        return instance;
    }

    /**
     * Take in the terminal object and run it to perform actual actions.
     *
     * @throws Exception
     */
    public void start() throws Exception {

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        // Flag that determines whether terminal continues to run or not
        // Default: true
        boolean continueExecution = true;
        Response res = new Response();

        String command = null;

        res.reply = NormalMessage.START_MESSAGE;
        this.display.updateUI(res);
        res.reply = NormalMessage.WELCOME_MESSAGE;
        this.display.updateUI(res);
        try (Scanner scanner = new Scanner(this.display.getInputStream())) {
            while (continueExecution) {
                this.taskManager.displayNextCommandRequest();
                this.display.getInputStream();

                try {
                    command = scanner.nextLine();
                } catch (NullPointerException e) {
                    logger.info(e.getMessage());
                } catch (NoSuchElementException e) {
                    continue;
                }

                // Passes string to Statement.java to parse into a command
                try {
                    this.parser.parse(command).execute(this.taskManager, false);
                } catch (InvalidParameterException e) {
                    res.reply = e.getMessage();
                    this.display.updateUI(res);
                } catch (CommandExecuteException e) {
                    res.reply = e.getMessage();
                    this.display.updateUI(res);
                } catch (ParseException e) {
                    res.reply = e.getMessage();
                    this.display.updateUI(res);
                } catch (DataException e) {
                    res.reply = e.getMessage();
                    this.display.updateUI(res);
                }
                continueExecution = this.taskManager.getContinueExecution();
            }
        }
    }
}
