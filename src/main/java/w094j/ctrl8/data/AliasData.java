package w094j.ctrl8.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import w094j.ctrl8.database.IStorableElement;
import w094j.ctrl8.exception.DataException;

/** This class contains all data about alias that are needed in alias-related operation
 */
public class AliasData implements IStorableElement {

    private Map<String, String> aliasMap;

    /**
     * Default constructor of AliasData
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

    /** Return the Alias Map
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

    public boolean isEmpty() {
        if (this.aliasMap == null) {
            return true;
        }
        return false;
    }
    
    /** Check whether the data structure is valid
     * return isValid
     */
    @Override
    public boolean isValid() {
        // checks the validity of the data structure
        // Ensures that the mapping does not contain any null keys or values.
        // check for a key that is null, that is illegal
        if (this.aliasMap.containsKey(null)) {
            return false;
        }
        if (this.aliasMap.containsValue(null)) {
            return false;
        }
        return true;
    }

    /** Set the alias map to aliasMap
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
            int length = alias.length();
            String subAlias = alias;
            while (length > 0) {
                subAlias = subAlias.substring(0, length);
                value = this.aliasMap.get(subAlias);
                if (value != null) {
                    return value + alias.substring(length, alias.length());
                }
                length--;
            }
            if (length == 0) {
                throw new DataException(
                        "No corresponding value for any sub-Alias of Alias("
                                + alias + ").");
            }
        }
        return value;
    }
}
