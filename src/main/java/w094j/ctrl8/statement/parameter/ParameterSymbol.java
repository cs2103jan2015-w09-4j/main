package w094j.ctrl8.statement.parameter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enum to classify the different kinds of parameter symbols.
 *
 * @author Han Liang Wee, Eric(A0065517A)
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

    private static Logger logger = LoggerFactory
            .getLogger(ParameterSymbol.class);
    private static Map<String, ParameterSymbol> parameterLookup = new HashMap<String, ParameterSymbol>();
    private static Pattern parameterPattern;

    private String symbol;

    static {
        String delim = "";
        String regex = "^(";
        for (ParameterSymbol eaParamSymbol : EnumSet
                .allOf(ParameterSymbol.class)) {
            parameterLookup.put(eaParamSymbol.toString(), eaParamSymbol);
            regex += delim;
            regex += Pattern.quote(parameterLookup.toString());
            delim = "|";
        }
        regex += ")(\\s|$)";
        parameterPattern = Pattern.compile(regex);
        logger.info("Parameter Parser initialized with REGEX:" + regex);
    }

    private ParameterSymbol(String symbol) {
        this.symbol = symbol;
    }

    public static Parameter parse(String parameterString) {
        return null;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

}
