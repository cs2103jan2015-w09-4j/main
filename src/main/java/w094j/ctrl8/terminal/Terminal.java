package w094j.ctrl8.terminal;

import java.io.InputStream;
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
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.TaskManager;

public class Terminal {
    private static Terminal instance;
    private static Logger logger = LoggerFactory.getLogger(Terminal.class);
    private static Parser parser;
    private Display display;
    private TaskManager taskManager;

    private Terminal(TerminalConfig terminalConfig) {
        assert (terminalConfig.isValid());
        this.taskManager = TaskManager.getInstance();
        this.display = Display.getInstance();
        parser = Parser.getInstance();
    }

    /**
     * Gets the current instance of the Terminal.
     *
     * @return the current instance.
     */
    public static Terminal getInstance() {
        if (instance == null) {
            instance = initInstance(new TerminalConfig());
        }

        return instance;
    }

    /**
     * Creates a Task Manager
     *
     * @return return the Task manager.
     */
    public static Terminal initInstance(TerminalConfig config) {
        if (instance != null) {
            throw new RuntimeException(
                    "Cannot initialize when it was initialized.");
        } else {
            // TO-DO put in config when config is done
            instance = new Terminal(config);
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
        Response res = new Response();
        String command = null;

        while (continueExecution) {
            this.taskManager.displayNextCommandRequest();
            InputStream input = this.display.getInputStream();

            try {
                command = new Scanner(input, "UTF-8").useDelimiter("\\A")
                        .next();
            } catch (NullPointerException e) {
                logger.info(e.getMessage());
            } catch (NoSuchElementException e) {
                continue;
            }

            // Passes string to Statement.java to parse into a command
            try {
                parser.parse(command).execute(this.taskManager);
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
