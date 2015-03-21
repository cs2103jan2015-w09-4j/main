package w094j.ctrl8.parse;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.CommandConfig;
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
            System.out.println(eaCommand);
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
     * Finds the Enum that corresponds to the command given in the nextLine.
     *
     * @param commandString
     *            command to be matched with the respective Enum.
     * @return Command Enum if found, null otherwise.
     */
    public CommandType parse(String commandString) {
        Matcher commandMatcher = this.commandPattern.matcher(commandString);

        if (commandMatcher.find()) {
            return this.commandLookup.get(commandMatcher.group().trim()
                    .toLowerCase());
        } else {
            return null;
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
