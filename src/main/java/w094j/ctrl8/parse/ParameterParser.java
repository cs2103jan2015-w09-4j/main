package w094j.ctrl8.parse;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.ParameterConfig;
import w094j.ctrl8.statement.parameter.CategoryParameter;
import w094j.ctrl8.statement.parameter.DeadlineParameter;
import w094j.ctrl8.statement.parameter.DescriptionParameter;
import w094j.ctrl8.statement.parameter.LocationParameter;
import w094j.ctrl8.statement.parameter.Parameter;
import w094j.ctrl8.statement.parameter.ParameterContainer;
import w094j.ctrl8.statement.parameter.ParameterType;
import w094j.ctrl8.statement.parameter.PriorityParameter;
import w094j.ctrl8.statement.parameter.ReminderParameter;
import w094j.ctrl8.statement.parameter.StartTimeParameter;
import w094j.ctrl8.statement.parameter.TitleParameter;

/**
 * Parameter Parser.
 */
public class ParameterParser {

    private String bannedSymbolsRegex;

    private Logger logger = LoggerFactory.getLogger(ParameterType.class);
    private Map<Character, ParameterType> parameterLookup = new HashMap<Character, ParameterType>();

    private Pattern parameterPattern;

    /**
     * Creates a Parameter Parser.
     *
     * @param config
     *            configuration for the parser.
     */
    public ParameterParser(ParameterConfig config) {

        for (ParameterType eaSymbol : EnumSet.allOf(ParameterType.class)) {
            this.parameterLookup.put(config.get(eaSymbol), eaSymbol);
        }

        String delim = "";
        String regex = "(";
        String symbolSelection = "";
        for (Character eaParamSymbol : this.parameterLookup.keySet()) {
            symbolSelection += delim;
            symbolSelection += Pattern.quote(Character.toString(eaParamSymbol));
            delim = "|";
        }
        regex += symbolSelection;
        regex += ")\\{([^\\r\\n\\{\\}" + symbolSelection + "]+)?\\}";
        this.logger.info(regex);
        this.parameterPattern = Pattern.compile(regex);
        this.bannedSymbolsRegex = "(" + symbolSelection + "|\\{|\\})";
        this.logger.info("Parameter Parser initialized with REGEX:"
                + this.parameterPattern.pattern());
        this.logger.info("Banned Symbols Regex:" + this.bannedSymbolsRegex);

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
    private static Parameter createParameter(ParameterType symbol,
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

    /**
     * @return the bannedSymbolsRegex
     */
    public String getBannedSymbolsRegex() {
        return this.bannedSymbolsRegex;
    }

    /**
     * Parses the parameter taken from the terminal.
     *
     * @param parameterString
     * @return the container that contains all the parameters parsed in this
     *         string.
     */
    public ParameterContainer parse(String parameterString) {

        Matcher parameterMatcher = this.parameterPattern
                .matcher(parameterString);

        List<Parameter> parameterList = new ArrayList<>();
        while (parameterMatcher.find()) {
            String eaParameter = parameterMatcher.group();
            Character eaParameterSymbol = eaParameter.charAt(0);
            String eaParameterPayload = eaParameter.replaceAll(
                    this.bannedSymbolsRegex, "");
            parameterList.add(createParameter(
                    this.parameterLookup.get(eaParameterSymbol),
                    eaParameterPayload));
        }

        ParameterContainer parameterContainer = new ParameterContainer(
                parameterList);

        return parameterContainer;
    }

}
