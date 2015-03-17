package w094j.ctrl8.statement.parameter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@author A0065517A
/**
 * Enum to classify the different kinds of parameter symbols.
 */
public enum ParameterSymbol {

    /**
     * Category of the task. Inspiration from twitter! #HASHTAG!
     */
    CATEGORY("#"),
    /**
     * Deadline of the task, it can also mean the end time when used in
     * conjunction with START_TIME. - looks like a knife going across your
     * throat, if you do not complete by then... Then you will die!
     */
    DEADLINE("-"),
    /**
     * Description of the task. Need more details? +, which means add, add more
     * details!
     */
    DESCRIPTION("+"),
    /**
     * Description of the task. @ is the at sign, which can conveniently replace
     * the word 'at'. I am @ School.
     */
    LOCATION("@"),
    /**
     * Priority of the task. % is the percent sign, which will tell you
     * percentage importance.
     */
    PRIORITY("%"),
    /**
     * Sets a Reminder for the task. ! is a symbol one would observe in an
     * important email, reminding the user to reply or die.
     */
    REMINDER("!"),
    /**
     * Start time of the task. Well, ~ seems cool to denote start time.
     */
    START_TIME("~"),
    /**
     * Title of the task. = is used in equations, and on the right hand side you
     * will have a nice evaluated answer, which 'sums' up the whole problem.
     * Similarly, a title 'sums' up the whole task.
     */
    TITLE("=");

    private static String bannedSymbolsRegex;
    private static Logger logger = LoggerFactory
            .getLogger(ParameterSymbol.class);

    private static Map<String, ParameterSymbol> parameterLookup = new HashMap<String, ParameterSymbol>();
    private static Pattern parameterPattern;
    private String symbol;

    static {
        String delim = "";
        String regex = "(";
        String symbolSelection = "";
        for (ParameterSymbol eaParamSymbol : EnumSet
                .allOf(ParameterSymbol.class)) {
            parameterLookup.put(eaParamSymbol.toString(), eaParamSymbol);
            symbolSelection += delim;
            symbolSelection += Pattern.quote(eaParamSymbol.toString());
            delim = "|";
        }
        regex += symbolSelection;
        regex += ")\\{([^\\r\\n\\{\\}" + symbolSelection + "]+)?\\}";
        logger.info(regex);
        parameterPattern = Pattern.compile(regex);
        bannedSymbolsRegex = "(" + symbolSelection + "|\\{|\\})";
        logger.info("Parameter Parser initialized with REGEX:"
                + parameterPattern.pattern());
        logger.info("Banned Symbols Regex:" + bannedSymbolsRegex);
    }

    private ParameterSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the bannedSymbolsRegex
     */
    public static String getBannedSymbolsRegex() {
        return bannedSymbolsRegex;
    }

    /**
     * Parses the parameter taken from the terminal.
     *
     * @param parameterString
     * @return the container that contains all the parameters parsed in this
     *         string.
     */
    public static ParameterContainer parse(String parameterString) {

        Matcher parameterMatcher = parameterPattern.matcher(parameterString);

        List<Parameter> parameterList = new ArrayList<>();
        while (parameterMatcher.find()) {
            String eaParameter = parameterMatcher.group();
            String eaParameterSymbol = eaParameter.substring(0, 1);
            String eaParameterPayload = eaParameter.replaceAll(
                    bannedSymbolsRegex, "");
            parameterList
            .add(createParameter(
                    parameterLookup.get(eaParameterSymbol),
                    eaParameterPayload));
        }

        ParameterContainer parameterContainer = new ParameterContainer(
                parameterList);

        return parameterContainer;
    }

    /**
     * Initializes the appropriate Parameter based on the ParameterSymbol.
     *
     * @param symbol
     *            symbol of the parameter to be created.
     * @param payload
     *            payload to parse based on the parameter context.
     * @return parsed Parameter.
     */
    private static Parameter createParameter(ParameterSymbol symbol,
            String payload) {

        switch (symbol) {
            case CATEGORY :
                return new CategoryParameter(payload);
            case DEADLINE :
                return new DeadlineParameter(payload);
            case DESCRIPTION :
                return new DescriptionParameter(payload);
            case LOCATION :
                return new LocationParameter(payload);
            case PRIORITY :
                return new PriorityParameter(payload);
            case REMINDER :
                return new ReminderParameter(payload);
            case START_TIME :
                return new StartTimeParameter(payload);
            case TITLE :
                return new TitleParameter(payload);
            default :
                // The code should never reach here
                assert (false);
                return null;
        }
    }

    @Override
    public String toString() {
        return this.symbol;
    }

}
