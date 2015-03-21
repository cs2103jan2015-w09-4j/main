package w094j.ctrl8.database.config;

import java.util.Map;

import w094j.ctrl8.data.AliasData;

/**
 * Configuration file for Alias.
 */
public class AliasConfig implements IConfig {

    static String ACCEPTABLE_SYMBOL_REGEX = ParserConfig.SYMBOL_REGEX;
    private static Character ALIAS_CHARACTER_DEFAULT = '|';

    private Character aliasCharacter;
    private AliasData aliasData;

    /**
     * Creates a Alias Config object, creates an empty aliasData.
     */
    public AliasConfig() {
        this.aliasData = new AliasData();
    }

    /**
     * @return the aliasCharacter
     */
    public Character getAliasCharacter() {
        if (this.aliasCharacter == null) {
            return ALIAS_CHARACTER_DEFAULT;
        } else {
            return this.aliasCharacter;
        }
    }

    /**
     * @return the aliasData
     */
    public AliasData getAliasData() {
        return this.aliasData;
    }

    @Override
    public boolean isValid() {

        // Checks if the symbols are acceptable
        if (!Character.toString(this.aliasCharacter).matches(
                ACCEPTABLE_SYMBOL_REGEX)) {
            return false;
        }

        // checks the validity of the data structure
        // Ensures that the mapping does not contain any null keys or values.
        Map<String, String> aliasMapping = this.aliasData.getAliasMap();
        // check for a key that is null, that is illegal
        if (aliasMapping.containsKey(null)) {
            return false;
        }
        if (aliasMapping.containsValue(null)) {
            return false;
        }
        return true;
    }

    /**
     * @param aliasCharacter
     *            the aliasCharacter to set
     */
    public void setAliasCharacter(Character aliasCharacter) {
        this.aliasCharacter = aliasCharacter;
    }

    /**
     * @param aliasData
     *            the aliasData to set
     */
    public void setAliasData(AliasData aliasData) {
        this.aliasData = aliasData;
    }
}
