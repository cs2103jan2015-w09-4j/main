package w094j.run;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.Database;
import w094j.ctrl8.database.Factory;
import w094j.ctrl8.display.CLIDisplay;
import w094j.ctrl8.display.IDisplay;
import w094j.ctrl8.message.ErrorMessage;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.message.OptionsConstants;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.taskmanager.TaskManager;
import w094j.ctrl8.terminal.Terminal;

/**
 * Class to start the Task Manager.
 */
// @author A0112092W

public class Start {

    // create the command line parser
    private static CommandLineParser commandLineParser = new GnuParser();

    /**
     * Runs the Task Manager with the file path to extract the data from the
     * file.
     */

    // create the logger
    private static Logger logger = LoggerFactory.getLogger(Start.class);
    // create the Options
    private static Options optionList;
    private static Parser parser;
    private static Factory factory;
    private static Terminal terminal;
    private static TaskManager taskManager;
    private static IDisplay display;
    
    /**
     * @param args
     *            TODO
     */
    public static void main(String[] args) {
        logger.info(NormalMessage.START_MESSAGE);
        // add all existing Options

        addOptions();


        // The terminal that performs all the actions

        if (checkArgs(args)) {
            parseArgs(args);
           
            factory = new Factory(args);
          //these of the following should be done by factory when factory is completed
            terminal = Terminal.getInstance();
            display = CLIDisplay.getInstance();
            taskManager = TaskManager.getInstance();
            parser = Parser.getInstance();
            
        } else {
            // Default database and terminal will be created if no file path
            // specified.
            logger.info(NormalMessage.NO_FILEPATH_MESSAGE);
            factory = new Factory();
            //these of the following should be done by factory when factory is completed
            terminal = Terminal.getInstance();
            display = CLIDisplay.getInstance();
            taskManager = TaskManager.getInstance();
            parser = Parser.getInstance();
        }
        logger.info(NormalMessage.WELCOME_MESSAGE);
        terminal.runTerminal(taskManager,display,parser);
    }

    public static void printHelp() {
        printHelp(optionList);
    }

    

    // This will add all the existing options
    private static void addOptions() {
        optionList = new Options();
        OptionsConstants.addAllOptions();
        for (int i = 0; i < OptionsConstants.getOptionNumber(); i++) {
            optionList.addOption(OptionsConstants.getOption(i));
        }
    }

    // check if the args is valid
    private static boolean checkArgs(String[] args) {

        if (args.length == 0) {
            return false;
        }
        return true;
    }

    // This is the parser for the args
    private static Database parseArgs(String[] args) {
        try {
            // parse the command line arguments
            CommandLine line = commandLineParser.parse(optionList, args);

            // validate that file's Path has been set
            if (line.hasOption("filePath")) {
                try {
                    Database database = new Database(
                            line.getOptionValue("filePath"));
                    // print the value of filePath
                    logger.info("Opening file at "
                            + line.getOptionValue("filePath"));
                    return database;
                } catch (Exception e) {
                    logger.info("Something is wrong with your file");
                    System.exit(1);
                }
            }

            // validate that help has been set
            if (line.hasOption("help")) {
                printHelp(optionList);
            }
            return null;
        }

        catch (org.apache.commons.cli.ParseException e) {
            logger.info(ErrorMessage.OPTION_NOT_FOUND);
            printHelp(optionList);
        }
        return null;

    }

    // print the help message
    private static void printHelp(Options optionList) {
        HelpFormatter formatter = new HelpFormatter();
        formatter
                .printHelp(
                        "Ctrl-8",
                        "Welcome to Ctrl-8. Below are the options you can choose",
                        optionList,
                        "For more information please refer to https://github.com/cs2103jan2015-w09-4j/main",
                        true);
    }

}
