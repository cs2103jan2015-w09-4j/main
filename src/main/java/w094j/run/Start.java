package w094j.run;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.Database;
import w094j.ctrl8.display.CLIDisplay;
import w094j.ctrl8.display.Display;
import w094j.ctrl8.message.ErrorMessage;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.message.OptionsConstants;
import w094j.ctrl8.pojo.Config;
import w094j.ctrl8.terminal.Terminal;

/**
 * Class to start the Task Manager.
 */
// @author A0112092W

public class Start {

    /**
     * Runs the Task Manager with the file path to extract the data from the
     * file.
     * 
     * @param args
     *            TODO
     */

    // create the logger
    private static Logger logger = LoggerFactory.getLogger(Start.class);
    // create the command line parser
    private static CommandLineParser parser = new GnuParser();
    // create the Options
    private static Options optionList;

    public static void main(String[] args) {
        logger.info(NormalMessage.START_MESSAGE);

        // add all existing Options
        addOptions();

        if (checkArgs(args)) {

            parseArgs(args);
            Display display = new CLIDisplay();
            Terminal terminal = new Terminal(Config.parseArgs(args), display);
            logger.info(NormalMessage.WELCOME_MESSAGE);
            terminal.runTerminal();
        } else {
            printHelp();
        }

    }

    // This will add all the existing options
    private static void addOptions() {
        optionList = new Options();
        OptionsConstants.addAllOptions();
        for (int i = 0; i < OptionsConstants.getOptionNumber(); i++) {
            optionList.addOption(OptionsConstants.getOption(i));
        }
    }

    // This is the parser for the args
    private static void parseArgs(String[] args) {
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(optionList, args);

            // validate that file's Path has been set
            if (line.hasOption("filePath")) {
                try {
                    // initiate the database with the file's path
                    Database database = new Database(
                            line.getOptionValue("filePath"));
                    // print the value of filePath
                    logger.info("Opening file at "
                            + line.getOptionValue("filePath"));
                } catch (Exception e) {
                    logger.info("Something is wrong with your file");
                    System.exit(1);
                }
            }

            // validate that help has been set
            if (line.hasOption("help")) {
                printHelp(optionList);
            }
        }

        catch (org.apache.commons.cli.ParseException e) {
            logger.info(ErrorMessage.OPTION_NOT_FOUND);
            printHelp(optionList);
        }

    }

    // check if the args is valid
    public static boolean checkArgs(String[] args) {

        if (args.length == 0) {
            return false;
        }
        return true;
    }

    public static void printHelp() {
        printHelp(optionList);
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
