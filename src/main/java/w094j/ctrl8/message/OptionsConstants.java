package w094j.ctrl8.message;

import java.util.ArrayList;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 * Class to store all Options.
 * 
 * @author Chen Tze Cheng (A0112092W)
 */
public class OptionsConstants {
    CommandLineParser parser = new GnuParser();
    // create the Options

    static org.apache.commons.cli.Option help = OptionBuilder
            .withDescription("Display the help message of the program.")
            .withLongOpt("help").create("h");

    static org.apache.commons.cli.Option filePath = OptionBuilder
            .withDescription("Enter the file's path").hasArg()
            .withLongOpt("filePath").create("f");

    static ArrayList optionArray = new ArrayList<org.apache.commons.cli.Option>();

    public static void addAllOptions() {
        optionArray.add(filePath);
        optionArray.add(help);
    }

    public static int getOptionNumber() {
        return optionArray.size();
    }

    // return the option in the array list with it's index number input
    public static org.apache.commons.cli.Option getOption(int i) {
        try {
            return (Option) optionArray.get(i);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    // return the option in the array list with the same option name input
    public static org.apache.commons.cli.Option getOption(String optionName) {
        try {
            for (int i = 0; i < optionArray.size(); i++) {
                if (optionName.equals(((Option) optionArray.get(i))
                        .getLongOpt())) {
                    return (Option) optionArray.get(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
