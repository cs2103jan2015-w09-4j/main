package w094j.ctrl8.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import w094j.ctrl8.exception.DataException;

/**
 */
public class AliasData {

    private Map<String, String> aliasMap;

    /**
     *
     */
    public AliasData() {
        this.aliasMap = new HashMap<String, String>();
    }

    /**
     * Adds an alias to the data structure.
     *
     * @param alias
     *            the alias to associate with value.
     * @param value
     *            the value to associate with alias.
     * @return the previous value associated with alias, or null if there is
     *         none.
     */
    public String addAlias(String alias, String value) {
        assert (alias != null);
        assert (value != null);
        assert (!value.equals(""));
        assert (!value.equals(""));
        return this.aliasMap.put(alias, value);
    }

    /**
     * Deletes an alias.
     *
     * @param alias
     *            to be deleted.
     * @throws DataException
     *             when the alias does not exist.
     */
    public void deleteAlias(String alias) throws DataException {
        if (this.aliasMap.remove(alias) == null) {
            throw new DataException("Alias(" + alias + ") does not exist.");
        }
    }

    /**
     * @return the aliasMap
     */
    public Map<String, String> getAliasMap() {
        return this.aliasMap;
    }

    /**
     * Gets the set of <alias, value> mappings.
     *
     * @return set of <alias, value> mappings.
     */
    public Set<Map.Entry<String, String>> getAllAliases() {
        return this.aliasMap.entrySet();
    }

    /**
     * @param aliasMap
     *            the aliasMap to set
     */
    public void setAliasMap(Map<String, String> aliasMap) {
        this.aliasMap = aliasMap;
    }

    /**
     * Finds the respective value for the alias.
     *
     * @param alias
     *            alias should exist in the data structure.
     * @return value mapped to alias.
     * @throws DataException
     *             when the alias does not exist.
     */
    public String toValue(String alias) throws DataException {
        assert (alias != null);
        String value = this.aliasMap.get(alias);
        if (value == null) {
            throw new DataException("No corresponding value for Alias(" + alias
                    + ").");
        }
        return value;
    }
    
    public boolean isEmpty(){
        if(this.aliasMap == null){
            return true;
        }
        return false;
    }
}
