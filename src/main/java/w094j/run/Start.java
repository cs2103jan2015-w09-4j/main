package w094j.run;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.Factory;
import w094j.ctrl8.message.CommandLineOptions;
import w094j.ctrl8.message.ErrorMessage;
import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.terminal.Terminal;

/**
 * Class to start the Task Manager.
 */
// @author A0112092W

public class Start {

    // create the command line parser
    private static CommandLineParser commandLineParser = new GnuParser();

    private static Factory factory;

    /**
     * Runs the Task Manager with the file path to extract the data from the
     * file.
     */

    // create the logger
    private static Logger logger = LoggerFactory.getLogger(Start.class);
    // create the Options
    private static Options optionList;
    private static Terminal terminal;

    /**
     * @param args
     *            TODO
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        logger.info(NormalMessage.START_MESSAGE);

        // add all existing Options
        optionList = CommandLineOptions.getOptionList();

        // The terminal that performs all the actions
        String filePath = parseArgs(args);

        factory = new Factory(filePath);

        logger.info(NormalMessage.WELCOME_MESSAGE);

        Terminal terminal = Terminal.getInstance();
        terminal.start();
    }

    public static void printHelp() {
        printHelp(optionList);
    }

    /**
     * Parses the args of the program. Exit if necessary. When the help option
     * is set, exit the program immediately. If the file option is not set,
     * return the default.
     *
     * @param args
     * @return Database object
     */
    private static String parseArgs(String[] args) {

        try {

            // parse the command line arguments
            CommandLine line = commandLineParser.parse(optionList, args);

            // validate that help has been set
            // exit if -h is used
            if (line.hasOption(CommandLineOptions.HELP.toString())) {
                printHelp(optionList);
                System.exit(1);
                return null;
            }

            // validate that file's Path has been set
            if (line.hasOption("filePath")) {
                String filePath = line.getOptionValue("filePath");

                // print the value of filePath
                logger.info("Database file is at:" + filePath);

                return filePath;
            } else {
                return null;
            }

        } catch (org.apache.commons.cli.ParseException e) {
            logger.info(ErrorMessage.OPTION_NOT_FOUND);
            printHelp(optionList);
            System.exit(1);
            return null;
        }

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
