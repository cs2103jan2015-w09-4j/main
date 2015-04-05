package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;


/**
 * Configuration file for Alias.
 */
public class AliasConfig implements IStorableElement {

    static String ACCEPTABLE_SYMBOL_REGEX = ParserConfig.SYMBOL_REGEX;
    private static Character ALIAS_CHARACTER_DEFAULT = '|';

    private Character aliasCharacter;

    /**
     * Creates a Alias Config object, creates an empty aliasData.
     */
    public AliasConfig() {
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

    @Override
    public boolean isValid() {

        // Checks if the symbols are acceptable
        if (!Character.toString(this.aliasCharacter).matches(
                ACCEPTABLE_SYMBOL_REGEX)) {
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

}
