package w094j.ctrl8.terminal;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.Database;
import w094j.ctrl8.database.config.TerminalConfig;
import w094j.ctrl8.display.Display;
import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.IParser;
import w094j.ctrl8.parse.statement.Statement;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112092W
public class Terminal {
    private static Terminal instance;
    private static Logger logger = LoggerFactory.getLogger(Terminal.class);
    private Database db;
    private Display display;
    private IParser parser;
    private ITaskManager taskManager;

    private Terminal(TerminalConfig terminalConfig, ITaskManager taskManager,
            Display display, IParser parser, Database db) {
        assert (terminalConfig.isValid());

        this.taskManager = taskManager;
        this.display = display;
        this.parser = parser;
        this.db = db;
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
     * @param db
     * @return return the Task manager.
     */
    public static Terminal initInstance(TerminalConfig config,
            ITaskManager taskManager, Display display, IParser parser,
            Database db) {
        if (instance != null) {
            throw new RuntimeException(
                    "Cannot initialize Terminal as it was initialized before.");
        } else {
            // TO-DO put in config when config is done
            instance = new Terminal(config, taskManager, display, parser, db);
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

        // Flag that determines whether terminal continues to run or not
        // Default: true
        boolean continueExecution = true;

        String command = null;

        this.display.welcome();
        try (Scanner scanner = new Scanner(this.display.getInputStream())) {
            while (continueExecution) {
                this.display.getInputStream();

                try {
                    command = scanner.nextLine();
                } catch (NullPointerException e) {
                    logger.info(e.getMessage());
                } catch (NoSuchElementException e) {
                    continue;
                }

                // Passes string to Statement.java to parse into a command
                Response res = new Response();
                try {
                    Statement statement = this.parser.parse(command);
                    res = statement.execute(this.taskManager, false);
                    this.db.saveToStorage();
                    this.display.updateUI(res);
                    continueExecution = res.isContinueExecution();
                    continueExecution = true;
                } catch (DataException | ParseException
                        | CommandExecuteException recoverableException) {
                    res.setException(recoverableException);
                    this.display.updateUI(res);
                } catch (GeneralSecurityException | IOException irrecoverableException) {
                    continueExecution = true;
                    res.setException(irrecoverableException);
                    this.display.updateUI(res);
                } catch (Exception e) {
                    continueExecution = true;
                    res.setException(e);
                    this.display.updateUI(res);
                }
            }
        }
    }
}
