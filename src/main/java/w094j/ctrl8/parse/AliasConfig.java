package w094j.ctrl8.parse;

import w094j.ctrl8.data.AliasData;

public class AliasConfig {

    private char aliasCharacter;
    private AliasData aliasData;

    public AliasConfig() {
    }

    /**
     * @return the aliasCharacter
     */
    public char getAliasCharacter() {
        return this.aliasCharacter;
    }

    /**
     * @param aliasCharacter
     *            the aliasCharacter to set
     */
    public void setAliasCharacter(char aliasCharacter) {
        this.aliasCharacter = aliasCharacter;
    }

    /**
     * @return the aliasData
     */
    public AliasData getAliasData() {
        return aliasData;
    }

    /**
     * @param aliasData the aliasData to set
     */
    public void setAliasData(AliasData aliasData) {
        this.aliasData = aliasData;
    }
}
