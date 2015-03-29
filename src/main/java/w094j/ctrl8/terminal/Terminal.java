package w094j.ctrl8.terminal;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.TerminalConfig;
import w094j.ctrl8.display.IDisplay;
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

    private Terminal() {
        // TODO Auto-generated constructor stub
    }

    private Terminal(TerminalConfig terminalConfig) {
        assert (terminalConfig.isValid());

    }

    /**
     * Gets the current instance of the Terminal.
     *
     * @return the current instance.
     */
    public static Terminal getInstance() {
        if(instance == null){
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
            instance = new Terminal();
            logger.debug("terminal is initialized");
        }
        return instance;
    }

    /**
     * Take in the terminal object and run it to perform actual actions.
     * 
     * @param taskManager
     * @param terminal
     * @param display
     * @param parser 
     * @throws IOException
     */
    public void runTerminal(TaskManager taskManager, IDisplay display,
            Parser parser) {
        // Flag that determines whether terminal continues to run or not
        // Default: true
        boolean continueExecution = true;
        Response res = new Response();
        String command = null;

        while (continueExecution) {
            taskManager.displayNextCommandRequest();
            InputStream input = display.getInputStream();

            try {
                command = new Scanner(input, "UTF-8").useDelimiter("\\A")
                        .next();
            } catch (NullPointerException e) {
                logger.info(e.getMessage());
            } catch (NoSuchElementException e){
                continue;
            }

            // Passes string to Statement.java to parse into a command
            try {
                parser.parse(command).execute(taskManager);
            } catch (InvalidParameterException e) {
                res.reply = e.getMessage();
                display.updateUI(res);
            } catch (CommandExecuteException e) {
                res.reply = e.getMessage();
                display.updateUI(res);
            } catch (ParseException e) {
                res.reply = e.getMessage();
                display.updateUI(res);
            } catch (DataException e) {
                res.reply = e.getMessage();
                display.updateUI(res);
            }
            continueExecution = taskManager.getContinueExecution();
        }
    }
}
