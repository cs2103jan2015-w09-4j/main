package w094j.ctrl8.parse;

import java.util.Map;

public class AliasConfig {

    private char aliasCharacter;
    private Map<String, String> aliasMap;

    public AliasConfig() {
    }

    /**
     * @return the aliasMap
     */
    public Map<String, String> getAliasMap() {
        return this.aliasMap;
    }

    /**
     * @param aliasMap
     *            the aliasMap to set
     */
    public void setAliasMap(Map<String, String> aliasMap) {
        this.aliasMap = aliasMap;
    }

    /**
     * @return the aliasCharacter
     */
    public char getAliasCharacter() {
        return aliasCharacter;
    }

    /**
     * @param aliasCharacter the aliasCharacter to set
     */
    public void setAliasCharacter(char aliasCharacter) {
        this.aliasCharacter = aliasCharacter;
    }

}
