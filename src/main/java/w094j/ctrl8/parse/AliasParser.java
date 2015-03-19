package w094j.ctrl8.parse;

import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.ParseException;

/**
 * Parser to replace all aliases with the actual string.
 */
public class AliasParser {

    /**
     * Regex to match all Alias, the delimiter is filled at runtime.
     */
    private static final String ALIAS_REGEX_FORMAT = "(?<!\\\\)%1$s[\\w]+";
    private static Logger logger = LoggerFactory.getLogger(AliasParser.class);

    private Map<String, String> aliasMap;
    private Pattern aliasPattern;

    /**
     * Creates an Alias Parser with the specified parameters
     * 
     * @param aliasData
     */
    public AliasParser(AliasConfig aliasData) {
        this.aliasMap = aliasData.getAliasMap();
        StringBuilder sb = new StringBuilder();
        // Send all output to the Appendable object sb
        try (Formatter formatter = new Formatter(sb, Locale.getDefault())) {
            formatter.format(ALIAS_REGEX_FORMAT, Pattern.quote(Character
                    .toString(aliasData.getAliasCharacter())));
            this.aliasPattern = Pattern.compile(sb.toString());
        }
    }

    /**
     * Replaces all aliases in inputToReplace to its aliases.
     *
     * @param inputToReplace
     *            the input string to replace aliases.
     * @return string with all aliases replaced with their actual text.
     * @throws ParseException
     *             when the alias cannot be found in the lookup table.
     */
    public String replaceAllAlias(String inputToReplace) throws ParseException {
        Matcher aliasMatcher = this.aliasPattern.matcher(inputToReplace);
        StringBuilder sb = new StringBuilder();
        int previousMatchEnd = 0;
        while (aliasMatcher.find()) {
            String eaAlias = aliasMatcher.group();
            String eaAliasReplaced = this.aliasMap.get(eaAlias.substring(1));
            if (eaAliasReplaced == null) {
                logger.info(inputToReplace + ": The alias(" + eaAlias
                        + ") cannot be found.");
                throw new ParseException("The alias(" + eaAlias
                        + ") cannot be found.");
            }
            logger.debug("Replace Alias(" + eaAlias + ") with \""
                    + eaAliasReplaced + "\".");
            sb.append(inputToReplace.substring(previousMatchEnd,
                    aliasMatcher.start()));
            previousMatchEnd = aliasMatcher.end();

            sb.append(eaAliasReplaced);
        }
        sb.append(inputToReplace.substring(previousMatchEnd));
        logger.info("\"" + inputToReplace + "\" was parsed to \""
                + sb.toString() + "\"");
        return sb.toString();
    }
}
