package w094j.ctrl8.database.config;

import java.util.HashMap;
import java.util.Map;

import w094j.ctrl8.statement.parameter.ParameterType;

/**
 * Configuration for parameter.
 */
public class ParameterConfig implements IConfig {

    static String ACCEPTABLE_SYMBOL_REGEX = ParserConfig.SYMBOL_REGEX;

    private static Character CATEGORY_DEFAULT = '#';
    private static Character DEADLINE_DEFAULT = '-';
    private static Character DESCRIPTION_DEFAULT = '+';
    private static Boolean IS_EXPLICIT_SHORT_MODE = true;
    private static Boolean IS_IMPLICIT_MODE = true;
    private static Character LOCATION_DEFAULT = '@';
    private static Character PRIORITY_DEFAULT = '%';
    private static Character REMINDER_DEFAULT = '!';
    private static Character START_TIME_DEFAULT = '~';
    private static Map<ParameterType, Character> SYMBOL_PAIRING_DEFAULTS = new HashMap<ParameterType, Character>();
    private static Character TITLE_DEFAULT = '=';

    private Boolean isExplicitShortMode;
    private Boolean isImplicitMode;
    private Map<ParameterType, Character> symbolPairing;

    static {

        // Initialize the Default Symbol Pairings
        SYMBOL_PAIRING_DEFAULTS.put(ParameterType.CATEGORY, CATEGORY_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(ParameterType.DEADLINE, DEADLINE_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(ParameterType.DESCRIPTION,
                DESCRIPTION_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(ParameterType.LOCATION, LOCATION_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(ParameterType.PRIORITY, PRIORITY_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(ParameterType.REMINDER, REMINDER_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(ParameterType.START_TIME,
                START_TIME_DEFAULT);
        SYMBOL_PAIRING_DEFAULTS.put(ParameterType.TITLE, TITLE_DEFAULT);
    }

    /**
     * Creates a Parameter Config with empty symbol pairings.
     */
    public ParameterConfig() {
        this.symbolPairing = new HashMap<ParameterType, Character>();
    }

    /**
     * Get the character for the the particular parameter, if the parameter does
     * not exist it will get the default.
     *
     * @param parameterSymbol
     *            the particular parameter for lookup.
     * @return the symbol for that parameter.
     */
    public Character get(ParameterType parameterSymbol) {
        assert (parameterSymbol != null);
        Character parameterCharacter = this.symbolPairing.get(parameterSymbol);
        if (parameterCharacter == null) {
            return SYMBOL_PAIRING_DEFAULTS.get(parameterSymbol);
        } else {
            return parameterCharacter;
        }
    }

    /**
     * @return the isExplicitShortMode
     */
    public boolean isExplicitShortMode() {
        if (this.isExplicitShortMode == null) {
            return IS_EXPLICIT_SHORT_MODE;
        } else {
            return this.isExplicitShortMode;
        }

    }

    /**
     * @return the isImplicitMode
     */
    public boolean isImplicitMode() {
        if (this.isImplicitMode == null) {
            return IS_IMPLICIT_MODE;
        } else {
            return this.isImplicitMode;
        }
    }

    @Override
    public boolean isValid() {

        // Flatten the user-defined mappings with the defaults
        Map<ParameterType, Character> currentMapping = Utils.flattenMapping(
                SYMBOL_PAIRING_DEFAULTS, this.symbolPairing,
                ParameterType.class);

        // Ensure that the mappings are one-one and also they matches the regex
        return Utils.isOneOneEnumMapping(currentMapping, ParameterType.class)
                && Utils.isEachValMatchRegex(currentMapping,
                        ACCEPTABLE_SYMBOL_REGEX);
    }

    /**
     * Sets the parameter with the characters.
     *
     * @param parameterSymbol
     *            parameter for which to set the character.
     * @param character
     *            sets the character for the parameter.
     */
    public void set(ParameterType parameterSymbol, Character character) {
        assert (parameterSymbol != null);
        assert (character != null);
        this.symbolPairing.put(parameterSymbol, character);
    }

    /**
     * @param isExplicitShortMode
     *            the isExplicitShortMode to set
     */
    public void setExplicitShortMode(boolean isExplicitShortMode) {
        this.isExplicitShortMode = isExplicitShortMode;
    }

    /**
     * @param isImplicitMode
     *            the isImplicitMode to set
     */
    public void setImplicitMode(boolean isImplicitMode) {
        this.isImplicitMode = isImplicitMode;
    }

}