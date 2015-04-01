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

/**
 * Parser to replace all aliases with the actual string.
 */
public class AliasParser {

    private static final String ALIAS_ESCAPED_CHARACTER_REGEX_FORMAT = "\\\\%1$s";
    /**
     * Regex to match all Alias, the delimiter is filled at runtime.
     */
    private static final String ALIAS_REGEX_FORMAT = "(?<!\\\\)%1$s[^\\s]+";
    private static Logger logger = LoggerFactory.getLogger(AliasParser.class);

    private Character aliasCharacter;
    private AliasData aliasData;
    private String aliasEscapedRegex;
    private Pattern aliasPattern;

    /**
     * Creates an Alias Parser with the specified parameters
     *
     * @param aliasConfig
     */
    public AliasParser(AliasConfig aliasConfig) {
        this.aliasData = aliasConfig.getAliasData();
        this.aliasCharacter = aliasConfig.getAliasCharacter();
        StringBuilder sb = new StringBuilder();
        // Send all output to the Appendable object sb
        try (Formatter formatter = new Formatter(sb, Locale.getDefault())) {
            formatter.format(ALIAS_REGEX_FORMAT, Pattern.quote(Character
                    .toString(aliasConfig.getAliasCharacter())));
            this.aliasPattern = Pattern.compile(sb.toString());
            logger.debug("Alias Regex(" + sb.toString() + ").");
            sb.setLength(0);
            formatter
                    .format(ALIAS_ESCAPED_CHARACTER_REGEX_FORMAT, Pattern
                            .quote(Character.toString(aliasConfig
                                    .getAliasCharacter())));
            logger.debug("Escaped alias Regex(" + sb.toString() + ").");
            this.aliasEscapedRegex = sb.toString();
        }
    }

    /**
     * Replaces all aliases in inputToReplace to its aliases, also unescapes all
     * alias characters.
     *
     * @param inputToReplace
     *            the input string to replace aliases.
     * @return string with all aliases replaced with their actual text.
     * @throws DataException
     *             when the alias cannot be found in the lookup table.
     */
    public String replaceAllAlias(String inputToReplace) throws DataException {
        Matcher aliasMatcher = this.aliasPattern.matcher(inputToReplace);
        StringBuilder sb = new StringBuilder();
        int previousMatchEnd = 0;
        while (aliasMatcher.find()) {
            String eaAlias = aliasMatcher.group();
            String eaAliasReplaced = this.aliasData.toValue(eaAlias
                    .substring(1));
            logger.debug("Replace Alias(" + eaAlias + ") with \""
                    + eaAliasReplaced + "\".");
            sb.append(inputToReplace.substring(previousMatchEnd,
                    aliasMatcher.start()));
            previousMatchEnd = aliasMatcher.end();

            sb.append(eaAliasReplaced);
        }
        sb.append(inputToReplace.substring(previousMatchEnd));
        String replacedString = sb.toString().replaceAll(
                this.aliasEscapedRegex, this.aliasCharacter.toString());
        logger.info("\"" + inputToReplace + "\" was parsed to \""
                + replacedString + "\"");
        return replacedString;
    }
}
