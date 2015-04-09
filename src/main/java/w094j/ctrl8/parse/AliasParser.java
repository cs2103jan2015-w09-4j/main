package w094j.ctrl8.parse;

import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.database.config.AliasConfig;
import w094j.ctrl8.exception.DataException;

//@author A0065517A
/**
 * Parser to replace all aliases with the actual value.
 */
public class AliasParser {

    /**
     * Regex to match all the escaped alias.
     *
     * <pre>
     *  <code>%1$s</code> is the alias character.
     * </pre>
     */
    private static final String ALIAS_ESCAPED_CHARACTER_REGEX_FORMAT = "\\\\%1$s";
    /**
     * Regex to match all valid alias, with its alias character
     *
     * <pre>
     *  <code>%1$s</code> is the alias character.
     * </pre>
     */
    private static final String ALIAS_REGEX_FORMAT = "(?<!\\\\)%1$s[^\\s%1$s]+";
    private static final Logger logger = LoggerFactory
            .getLogger(AliasParser.class);

    private Character aliasCharacter;
    private AliasData aliasData;
    private String aliasEscapedRegex;
    private Pattern aliasPattern;

    /**
     * Creates an Alias Parser with the alias config and data.
     *
     * @param aliasConfig
     *            configuration for the alias symbols.
     * @param aliasData
     *            data that has the pairings of <alias, value>.
     */
    public AliasParser(AliasConfig aliasConfig, AliasData aliasData) {

        this.aliasData = aliasData;
        this.aliasCharacter = aliasConfig.getAliasCharacter();

        // Create a StringBuilder and send all output to sb
        StringBuilder sb = new StringBuilder();

        try (Formatter formatter = new Formatter(sb, Locale.getDefault())) {

            // format the regex, entering the alias character into Regex
            formatter.format(ALIAS_REGEX_FORMAT, Pattern.quote(Character
                    .toString(aliasConfig.getAliasCharacter())));

            // Create the pattern object to store the Regex
            this.aliasPattern = Pattern.compile(sb.toString());
            logger.debug("Alias Regex(" + sb.toString() + ").");

            // reset the sb
            sb.setLength(0);

            // Format the escaped chracter Regex
            formatter
            .format(ALIAS_ESCAPED_CHARACTER_REGEX_FORMAT, Pattern
                    .quote(Character.toString(aliasConfig
                            .getAliasCharacter())));

            logger.debug("Escaped alias Regex(" + sb.toString() + ").");

            // Store only the regex, as we are going to use the function
            // replaceAll
            this.aliasEscapedRegex = sb.toString();
        }
    }

    /**
     * Checks if a given alias is valid. An alias is valid if and only if it
     * does not contain the alias character.
     *
     * @param alias
     * @return <code>true</code> if the alias is valid, <code>false</code>
     *         otherwise.
     */
    public boolean isValidAlias(String alias) {
        return !alias.contains(this.aliasCharacter.toString());
    }

    /**
     * Replaces all aliases in inputToReplace to its aliases, also unescapes all
     * escaped alias characters.
     *
     * @param inputToReplace
     *            the input string to replace aliases.
     * @return string with all aliases replaced with their actual text.
     * @throws DataException
     *             when the alias cannot be found in the lookup table.
     */
    public String replaceAllAlias(String inputToReplace) throws DataException {

        // Matcher to match aliases with their symbols
        Matcher aliasMatcher = this.aliasPattern.matcher(inputToReplace);

        // String builder to re-construct the string
        StringBuilder sb = new StringBuilder();
        int previousMatchEnd = 0;
        while (aliasMatcher.find()) {

            String eaAlias = aliasMatcher.group();
            sb.append(inputToReplace.substring(previousMatchEnd,
                    aliasMatcher.start()));

            // Alias's value, will throw exception if does not exist
            String eaAliasReplaced = this.aliasData.toValue(eaAlias
                    .substring(1));
            logger.debug("Replace Alias(" + eaAlias + ") with \""
                    + eaAliasReplaced + "\".");

            previousMatchEnd = aliasMatcher.end();
            sb.append(eaAliasReplaced);
        }

        // Replace all escaped alias characters with the alias character
        sb.append(inputToReplace.substring(previousMatchEnd));
        String replacedString = sb.toString().replaceAll(
                this.aliasEscapedRegex, this.aliasCharacter.toString());
        logger.info("\"" + inputToReplace + "\" was parsed to \""
                + replacedString + "\"");
        return replacedString;
    }
}
