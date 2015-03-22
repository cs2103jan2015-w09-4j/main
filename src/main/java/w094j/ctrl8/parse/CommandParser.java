package w094j.ctrl8.parse;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.CommandConfig;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.statement.CommandType;

/**
 * Parses a statement string, recognizing the command type.
 */
public class CommandParser {

    private Map<String, CommandType> commandLookup = new ConcurrentHashMap<String, CommandType>();
    private Pattern commandPattern;
    private Logger logger = LoggerFactory.getLogger(CommandType.class);

    CommandParser(CommandConfig commandConfig) {
        String delim = "";
        String regex = "^(";
        for (CommandType eaCommand : EnumSet.allOf(CommandType.class)) {
            this.commandLookup.put(eaCommand.toString(), eaCommand);
            regex += delim;
            regex += Pattern.quote(eaCommand.toString());
            delim = "|";
        }
        regex += ")(\\s|$)";
        this.commandPattern = Pattern.compile(regex);
        this.logger.info("Command Parser initialized with REGEX:" + regex);
    }

    /**
     * Parses the Statement to find the CommandType. This does a strict parsing
     * of the statement, "HiStOrY-ClEaR" and " add" are not acceptable.
     *
     * @param statement
     *            Statement provided for parsing.
     * @return Command Type of the statement.
     * @throws ParseException
     *             if there is some problem with the statement; null statement,
     *             no commands found in the statement.
     */
    public CommandType parse(String statement) throws ParseException {

        if (statement == null) {
            throw new ParseException("Statement cannot be null.");
        }

        Matcher commandMatcher = this.commandPattern.matcher(statement);

        if (commandMatcher.find()) {
            return this.commandLookup.get(commandMatcher.group().trim()
                    .toLowerCase());
        } else {
            throw new ParseException("Statement does not contain any commands.");
        }
    }

    /**
     * Removes the command keyword from a statement, if it exist.
     *
     * @param statement
     *            to remove the command keyword.
     * @return Statement without the keyword, leaving only the parameter(s).
     */
    public String removeCommandKeyword(String statement) {
        Matcher commandMatcher = this.commandPattern.matcher(statement);
        return commandMatcher.replaceFirst("");
    }
}
