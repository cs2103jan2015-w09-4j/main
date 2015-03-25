package w094j.ctrl8.parse;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private ParameterConfig config;
    private Set<String> escapableSymbols;

    private Pattern explicitParameterPattern;
    private Pattern explicitParameterPayloadPattern;

    private Pattern explicitShortParameterPattern;
    private Pattern explicitShortParameterPayloadPattern;
    private Logger logger = LoggerFactory.getLogger(ParameterType.class);
    private Map<Character, ParameterType> parameterLookup = new HashMap<Character, ParameterType>();

    /**
     * Creates a Parameter Parser.
     *
     * @param config
     *            configuration for the parser.
     */
    public ParameterParser(ParameterConfig config) {

        this.config = config;
        this.escapableSymbols = new HashSet<String>();

        for (ParameterType eaSymbol : EnumSet.allOf(ParameterType.class)) {
            this.escapableSymbols.add(this.config.get(eaSymbol).toString());
            this.parameterLookup.put(this.config.get(eaSymbol), eaSymbol);
        }
        this.escapableSymbols.add("{");
        this.escapableSymbols.add("}");
        this.escapableSymbols.add("\\");

        String delim = "";
        String symbolSelection = "";
        for (Character eaParamSymbol : this.parameterLookup.keySet()) {
            symbolSelection += delim;
            symbolSelection += Pattern.quote(Character.toString(eaParamSymbol));
            delim = "|";
        }

        delim = "";
        String escapableSymbolSelection = "";
        for (String eaSymbol : this.escapableSymbols) {
            escapableSymbolSelection += delim;
            escapableSymbolSelection += Pattern.quote(eaSymbol);
            delim = "|";
        }

        System.out.println(escapableSymbolSelection);

        String regexExplicit = "(^|\\s)(" + symbolSelection
                + ")\\{([^\\r\\n\\\\\\{\\}" + symbolSelection + "]|\\\\("
                + escapableSymbolSelection + ")|\\\\.){0,}\\}";
        String regexExplicitShort = "(^|\\s)(" + symbolSelection
                + ")([^\\s\\r\\n\\\\\\{\\}" + symbolSelection + "]|\\\\("
                + escapableSymbolSelection + ")|\\\\.){1,}";

        this.explicitParameterPattern = Pattern.compile(regexExplicit);
        this.explicitShortParameterPattern = Pattern
                .compile(regexExplicitShort);

        String explicitParameterPayloadRegex = "(?<=(^" + symbolSelection
                + ")\\{).{0,}(?=\\}$)";
        String explicitShortParameterPayloadRegex = "(?<=(^" + symbolSelection
                + ")).{1,}$";

        this.explicitParameterPayloadPattern = Pattern
                .compile(explicitParameterPayloadRegex);
        this.explicitShortParameterPayloadPattern = Pattern
                .compile(explicitShortParameterPayloadRegex);

        this.logger.info("Explicit parameter: Regex(" + regexExplicit + ")");
        this.logger.info("Explicit parameter payload: Regex("
                + explicitParameterPayloadRegex + ")");
        this.logger.info("Explicit short parameter: Regex("
                + regexExplicitShort + ")");
        this.logger.info("Explicit parameter payload: Regex("
                + explicitShortParameterPayloadRegex + ")");

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
     * Parses the parameter taken from the terminal.
     *
     * @param parameterString
     * @return the container that contains all the parameters parsed in this
     *         string.
     */
    public ParameterContainer parse(String parameterString) {

        List<Parameter> parameterList = new ArrayList<>();

        // Parse Explicit Long First
        parameterString = this.parse(parameterString, parameterList,
                this.explicitParameterPattern,
                this.explicitParameterPayloadPattern);
        this.logger.debug("Parameters after parsing explicit long: String("
                + parameterString + ")");

        // Parse Explicit Short Next if applicable
        if (this.config.isExplicitShortMode()) {
            parameterString = this.parse(parameterString, parameterList,
                    this.explicitShortParameterPattern,
                    this.explicitShortParameterPayloadPattern);
            this.logger
            .debug("Parameters after parsing explicit short: String("
                    + parameterString + ")");
        }

        // Parses Implicit for the rest
        if (this.config.isImplicitMode()) {
            this.parseImplicit(parameterString);
        }

        ParameterContainer parameterContainer = new ParameterContainer(
                parameterList);

        return parameterContainer;
    }

    /**
     * @param parameterString
     * @param parameterList
     * @param parameterPattern
     * @param parameterPayloadPattern
     * @return
     */
    private String parse(String parameterString, List<Parameter> parameterList,
            Pattern parameterPattern, Pattern parameterPayloadPattern) {
        Matcher parameterMatcher = parameterPattern.matcher(parameterString);

        while (parameterMatcher.find()) {
            String eaParameter = parameterMatcher.group().trim();
            Character eaParameterSymbol = eaParameter.charAt(0);

            // Matches the payload of the parameter
            Matcher explicitParameterPayloadMatcher = parameterPayloadPattern
                    .matcher(eaParameter);
            if (explicitParameterPayloadMatcher.find()
                    && (explicitParameterPayloadMatcher.groupCount() == 1)) {

                String eaParameterPayload = explicitParameterPayloadMatcher
                        .group();
                // unescape all the characters inside the payload
                for (Character eaParameterInMap : this.parameterLookup.keySet()) {
                    eaParameterPayload = eaParameterPayload.replaceAll("\\\\"
                            + Pattern.quote(eaParameterInMap.toString()),
                            eaParameterInMap.toString());
                }

                parameterList.add(createParameter(
                        this.parameterLookup.get(eaParameterSymbol),
                        eaParameterPayload));
            } else {
                throw new RuntimeException("Payload should contain ");
            }

        }
        return parameterMatcher.replaceAll("");
    }

    private String parseImplicit(String parameterString) {
        return null;
    }

}
