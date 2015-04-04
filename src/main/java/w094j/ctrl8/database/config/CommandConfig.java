package w094j.ctrl8.database.config;

import java.util.HashMap;
import java.util.Map;

import w094j.ctrl8.database.IStorableElement;
import w094j.ctrl8.statement.CommandType;

/**
 * Configuration options for Command.
 */
public class CommandConfig implements IStorableElement {

    /**
     * Accept only Capital letters; at least 1 character, maximum 3.
     */
    private static String ACCEPTABLE_SHORTCOMMAND_REGEX = "^[A-Z]{1,3}$";
    private static String ADD_DEFAULT = "A";
    private static String ALIAS_ADD_DEFAULT = "ALA";
    private static String ALIAS_DEFAULT = "AL";
    private static String ALIAS_DELETE_DEFAULT = "ALD";
    private static String DELETE_DEFAULT = "DEL";
    private static String DONE_DEFAULT = "D";
    private static String EXIT_DEFAULT = "E";
    private static String HELP_DEFAULT = "H";
    private static String HISTORY_CLEAR_DEFAULT = "HIC";
    private static String HISTORY_DEFAULT = "HI";
    private static String HISTORY_UNDO_DEFAULT = "HIU";
    /**
     * The implicit command is Add command.
     */
    private static CommandType IMPLICIT_COMMAND_DEFAULT = CommandType.ADD;
    private static String MODIFY_DEFAULT = "M";
    private static String SEARCH_DEFAULT = "SE";
    private static Boolean SHORT_COMMAND_MODE = true;
    private static Map<CommandType, String> SYMBOL_PAIRING_DEFAULTS = new HashMap<CommandType, String>();

    private static String VIEW_DEFAULT = "V";
    private CommandType implicitCommand;

    private Boolean shortCommandMode;
    private Map<CommandType, String> symbolPairing;

    static {
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.ADD, ADD_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.ALIAS, ALIAS_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.ALIAS_ADD, ALIAS_ADD_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.ALIAS_DELETE,
                ALIAS_DELETE_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.DELETE, DELETE_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.DONE, DONE_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.EXIT, EXIT_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.HELP, HELP_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.HISTORY_CLEAR,
                HISTORY_CLEAR_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.HISTORY, HISTORY_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.HISTORY_UNDO,
                HISTORY_UNDO_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.MODIFY, MODIFY_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.SEARCH, SEARCH_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(CommandType.VIEW, VIEW_DEFAULT);
    }

    /**
     * Creates a Command Config with empty symbol pairings.
     */
    public CommandConfig() {
        this.symbolPairing = new HashMap<CommandType, String>();
    }

    /**
     * Gets a particular command's short command string. If the config is not
     * set by the user, defaults will be used.
     *
     * @param commandType
     *            type of command.
     * @return short command string for the commandType.
     */
    public String get(CommandType commandType) {
        assert (commandType != null);
        if (this.symbolPairing.get(commandType) == null) {
            return SYMBOL_PAIRING_DEFAULTS.get(commandType);
        } else {
            return this.symbolPairing.get(commandType);
        }
    }

    /**
     * @return the implicitCommand
     */
    public CommandType getImplicitCommand() {
        if (this.implicitCommand == null) {
            return IMPLICIT_COMMAND_DEFAULT;
        } else {
            return this.implicitCommand;
        }

    }

    /**
     * @return the shortCommandMode
     */
    public boolean isShortCommandMode() {
        if (this.shortCommandMode == null) {
            return SHORT_COMMAND_MODE;
        } else {
            return this.shortCommandMode;
        }
    }

    @Override
    public boolean isValid() {
        // Flatten the user-defined mappings with the defaults
        Map<CommandType, String> currentMapping = Utils.flattenMapping(
                SYMBOL_PAIRING_DEFAULTS, this.symbolPairing, CommandType.class);

        // Ensure that the mappings are one-one and also they matches the regex
        return Utils.isOneOneEnumMapping(currentMapping, CommandType.class)
                && Utils.isEachValMatchRegex(currentMapping,
                        ACCEPTABLE_SHORTCOMMAND_REGEX);

    }

    /**
     * Sets a particular command with the short command string.
     *
     * @param commandType
     *            particular command to set the short command string.
     * @param shortCommandString
     *            short command string to set.
     */
    public void set(CommandType commandType, String shortCommandString) {
        assert (commandType != null);
        assert (shortCommandString != null);
        this.symbolPairing.put(commandType, shortCommandString);
    }

    /**
     * @param implicitCommand
     *            the implicitCommand to set
     */
    public void setImplicitCommand(CommandType implicitCommand) {
        this.implicitCommand = implicitCommand;
    }

    /**
     * @param shortCommandMode
     *            the shortCommandMode to set
     */
    public void setShortCommandMode(boolean shortCommandMode) {
        this.shortCommandMode = shortCommandMode;
    }

}
