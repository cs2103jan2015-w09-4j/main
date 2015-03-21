package w094j.ctrl8.database.config;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utilities for config classes.
 */
public class Utils {

    /**
     * Returns the flattened Mappings; for those values that the userMapping are
     * missing, the values in defaultMapping are used. The default setting must
     * have all the Enums as keys.
     *
     * @param defaultMapping
     *            default mapping.
     * @param userMapping
     *            user-set mapping.
     * @param keyType
     *            keys, enum.
     * @return flattened mapping with all settings of the enum.
     */
    public static <K extends Enum<K>, V> Map<K, V> flattenMapping(
            Map<K, V> defaultMapping, Map<K, V> userMapping, Class<K> keyType) {

        assert (defaultMapping.size() == EnumSet.allOf(keyType).size());

        Map<K, V> flattenedMapping = new HashMap<>();
        for (K eaKey : EnumSet.allOf(keyType)) {
            if (userMapping.get(eaKey) == null) {
                flattenedMapping.put(eaKey, defaultMapping.get(eaKey));
            } else {
                flattenedMapping.put(eaKey, userMapping.get(eaKey));
            }
        }
        return flattenedMapping;

    }

    /**
     * Checks if each of the values in the mapping matches the regex provided.
     * The values will be serialized to a string to perform the regex match.
     *
     * @param flattenedMapping
     *            Flattened mapping of the Enum to the value.
     * @param regexForEachValue
     *            Regex for each value in the map.
     * @return <code>true</code> if each of the values matches,
     *         <code>false</code> otherwise.
     */
    public static <K extends Enum<K>, V> boolean isEachValMatchRegex(
            Map<K, V> flattenedMapping, String regexForEachValue) {

        for (V eaValue : flattenedMapping.values()) {
            if (!(eaValue.toString()).matches(regexForEachValue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the mapping(Enum, value) is one to one. One-One relationship
     * should not have any null keys or values. It is assumed that the mapping
     * is already flattened.
     *
     * @param flattenedMapping
     *            flattened mapping of the Enum to the value.
     * @param keyType
     *            the keys which are enums.
     * @return <code>true</code> if is one-one, otherwise <code>false</code>.
     */
    public static <K extends Enum<K>, V> boolean isOneOneEnumMapping(
            Map<K, V> flattenedMapping, Class<K> keyType) {

        // Ensures that all the types in enum is in the Map
        if (EnumSet.allOf(keyType).size() != flattenedMapping.keySet().size()) {
            return false;
        }
        // Ensures that the mapping is one-one
        if (!isOneOneMapping(flattenedMapping)) {
            return false;
        }
        return true;

    }

    /**
     * Checks if the mapping is one to one. One-One relationship should not have
     * any null keys or values.
     *
     * @param mapping
     * @return <code>true</code> if is one-one, otherwise <code>false</code>.
     */
    public static <K, V> boolean isOneOneMapping(Map<K, V> mapping) {

        // check if null is one of the keys
        if (mapping.containsKey(null)) {
            return false;
        }
        // all keys are valid

        // Ensure that the mappings are one-one
        // this ensures that the keys set is of the right size as well
        Set<V> values = new HashSet<V>(mapping.values());
        if (values.contains(null)) {
            return false;
        }
        if (values.size() != mapping.keySet().size()) {
            return false;
        }
        return true;

    }
}
