package w094j.ctrl8.message;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 *
 */
public enum CommandLineOptions {

    /**
     * TODO
     */
    FILE_PATH("filepath", "f", true, "Enter the file's path"),
    /**
     * TODO
     */
    HELP("help", "h", false, "Display the help message of the program.");

    // Stores the options into an array
    private static Options optionList;

    private String description;

    private boolean hasArguments;

    private String longOption;
    private String shortOption;
    static {

        optionList = new Options();
        for (CommandLineOptions eaCommandLineOption : CommandLineOptions
                .values()) {

            @SuppressWarnings("static-access")
            OptionBuilder optionBuilder = OptionBuilder.withLongOpt(
                    eaCommandLineOption.getLongOption()).withDescription(
                    eaCommandLineOption.getDescription());

            if (eaCommandLineOption.hasArguments()) {
                optionBuilder = OptionBuilder.hasArg();
            }

            @SuppressWarnings("static-access")
            Option option = optionBuilder.create(eaCommandLineOption
                    .getShortOption());

            optionList.addOption(option);
        }

    }

    private CommandLineOptions(String longOption, String shortOption,
            boolean hasArguments, String description) {
        this.longOption = longOption;
        this.shortOption = shortOption;
        this.hasArguments = hasArguments;
    }

    /**
     * @return the optionList
     */
    public static Options getOptionList() {
        return optionList;
    }

    /**
     * @param optionList
     *            the optionList to set
     */
    public static void setOptionList(Options optionList) {
        CommandLineOptions.optionList = optionList;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return the longOption
     */
    public String getLongOption() {
        return this.longOption;
    }

    /**
     * @return the shortOption
     */
    public String getShortOption() {
        return this.shortOption;
    }

    /**
     * @return the hasArguments
     */
    public boolean hasArguments() {
        return this.hasArguments;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param hasArguments
     *            the hasArguments to set
     */
    public void setHasArguments(boolean hasArguments) {
        this.hasArguments = hasArguments;
    }

    /**
     * @param longOption
     *            the longOption to set
     */
    public void setLongOption(String longOption) {
        this.longOption = longOption;
    }

    /**
     * @param shortOption
     *            the shortOption to set
     */
    public void setShortOption(String shortOption) {
        this.shortOption = shortOption;
    }

    @Override
    public String toString() {
        return this.longOption;
    }

}
