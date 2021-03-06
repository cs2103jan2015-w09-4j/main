package w094j.ctrl8.parse;

import java.security.InvalidParameterException;
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
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.statement.parameter.CategoryParameter;
import w094j.ctrl8.parse.statement.parameter.DeadlineParameter;
import w094j.ctrl8.parse.statement.parameter.DescriptionParameter;
import w094j.ctrl8.parse.statement.parameter.LocationParameter;
import w094j.ctrl8.parse.statement.parameter.Parameter;
import w094j.ctrl8.parse.statement.parameter.ParameterContainer;
import w094j.ctrl8.parse.statement.parameter.ParameterType;
import w094j.ctrl8.parse.statement.parameter.PriorityParameter;
import w094j.ctrl8.parse.statement.parameter.ReminderParameter;
import w094j.ctrl8.parse.statement.parameter.StartTimeParameter;
import w094j.ctrl8.parse.statement.parameter.TitleParameter;

/**
 * Parameter Parser.
 */
public class ParameterParser {

    private static final String DEADLINED_TASK_TO_KEYWORD = " due ";
    private static Character ESCAPE_CHARACTER = '\\';
    private static final String EXPLICIT_PARAMETER_PAYLOAD_REGEX_FORMAT = "(?<=(?:%1$s)%2$s).{0,}(?=%3$s)";
    private static final String EXPLICIT_PARAMETER_REGEX_FORMAT = "(?:^|\\s)(?:%1$s)%2$s(?:(?!\\s(?:%3$s)%4$s).)*%5$s";
    private static final String EXPLICIT_PARAMETER_SHORT_PAYLOAD_REGEX_FORMAT = "(?<=(?:%1$s)).{1,}";
    private static final String EXPLICIT_PARAMETER_SHORT_REGEX_FORMAT = "(?:^|\\s)(?:%1$s)(?:(?!\\s(?:%2$s))[^\\s]){1,}";
    private static final String IMPLICIT_DEADLINED_TASK_REGEX_FORMAT = "(?<!\\{[^\\}])(?:%1$s)(?:(?!\\s(?:%2$s)).)*(?![^\\{]{0,}\\})";
    private static final String IMPLICIT_TIMED_TASK_REGEX_FORMAT = "(?<!\\{[^\\}])(?:%2$s)(?:(?!\\s(?:%1$s)).)*(?:%3$s)(?:(?!\\s(?:%1$s)).)*(?![^\\{]{0,}\\})";
    private static final String IMPLICIT_TITLE_REGEX_FORMAT = "(?:\\s)[^%1$s](?:(?!\\s(?:%2$s)).)*(?![^\\{]{0,}\\})";
    private static final String TIMED_TASK_FROM_KEYWORD = " from ";
    private static final String TIMED_TASK_TO_KEYWORD = " to ";

    private ParameterConfig config;

    private String endDelimiterQuoted;
    private Set<String> escapableSymbols;

    private Pattern explicitParameterPattern;
    private Pattern explicitParameterPayloadPattern;
    private Pattern explicitShortParameterPattern;
    private Pattern explicitShortParameterPayloadPattern;

    private Pattern implicitDeadlinedTaskPattern;
    private Pattern implicitTimedTaskPattern;
    private Pattern implicitTitlePattern;
    private Logger logger = LoggerFactory.getLogger(ParameterType.class);
    private Map<Character, ParameterType> parameterLookup = new HashMap<Character, ParameterType>();
    private String startDelimiterQuoted;

    /**
     * Creates a Parameter Parser.
     *
     * @param config
     *            configuration for the parser.
     */
    ParameterParser(ParameterConfig config) {

        this.config = config;
        this.escapableSymbols = new HashSet<String>();

        for (ParameterType eaSymbol : EnumSet.allOf(ParameterType.class)) {
            this.escapableSymbols.add(this.config.get(eaSymbol).toString());
            this.parameterLookup.put(this.config.get(eaSymbol), eaSymbol);
        }
        this.escapableSymbols.add(this.config.getStartDelimiterSymbol()
                .toString());
        this.escapableSymbols.add(this.config.getEndDelimiterSymbol()
                .toString());
// this.escapableSymbols.add(ESCAPE_CHARACTER.toString());

        this.startDelimiterQuoted = Pattern.quote(this.config
                .getStartDelimiterSymbol().toString());
        this.endDelimiterQuoted = Pattern.quote(this.config
                .getEndDelimiterSymbol().toString());

        String delim = "";
        String symbolSelection = "";
        for (Character eaParamSymbol : this.parameterLookup.keySet()) {
            symbolSelection += delim;
            symbolSelection += Pattern.quote(Character.toString(eaParamSymbol));
            delim = "|";
        }

        delim = "";
        String escapableSymbolGroup = "";
        String escapableSymbolSelection = "";
        for (String eaSymbol : this.escapableSymbols) {
            escapableSymbolSelection += delim;
            escapableSymbolGroup += Pattern.quote(eaSymbol);
            escapableSymbolSelection += Pattern.quote(eaSymbol);
            delim = "|";
        }

        String regexExplicit = String.format(EXPLICIT_PARAMETER_REGEX_FORMAT,
                symbolSelection, this.startDelimiterQuoted, symbolSelection,
                this.startDelimiterQuoted, this.endDelimiterQuoted);

        String regexExplicitShort = String.format(
                EXPLICIT_PARAMETER_SHORT_REGEX_FORMAT, symbolSelection,
                symbolSelection);

        String implicitTimedTaskRegex = String.format(
                IMPLICIT_TIMED_TASK_REGEX_FORMAT, symbolSelection,
                TIMED_TASK_FROM_KEYWORD, TIMED_TASK_TO_KEYWORD);
        String implicitDeadlinedTaskRegex = String.format(
                IMPLICIT_DEADLINED_TASK_REGEX_FORMAT,
                DEADLINED_TASK_TO_KEYWORD, symbolSelection);
        String implicitTitleRegex = String.format(IMPLICIT_TITLE_REGEX_FORMAT,
                escapableSymbolGroup, escapableSymbolSelection);

        this.implicitTimedTaskPattern = Pattern.compile(implicitTimedTaskRegex);
        this.implicitDeadlinedTaskPattern = Pattern
                .compile(implicitDeadlinedTaskRegex);
        this.implicitTitlePattern = Pattern.compile(implicitTitleRegex);

        this.logger.debug(implicitTimedTaskRegex);
        this.logger.debug(implicitDeadlinedTaskRegex);
        this.logger.debug(implicitTitleRegex);

        this.explicitParameterPattern = Pattern.compile(regexExplicit);
        this.explicitShortParameterPattern = Pattern
                .compile(regexExplicitShort);

        String explicitParameterPayloadRegex = String.format(
                EXPLICIT_PARAMETER_PAYLOAD_REGEX_FORMAT, symbolSelection,
                this.startDelimiterQuoted, this.endDelimiterQuoted);

        String explicitShortParameterPayloadRegex = String.format(
                EXPLICIT_PARAMETER_SHORT_PAYLOAD_REGEX_FORMAT, symbolSelection);

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
     * @throws ParseException
     *             when the parsing of parameters encountered a problem.
     */
    public ParameterContainer parse(String parameterString)
            throws ParseException {

        List<Parameter> parameterList = new ArrayList<>();

        // Parses Implicit for tasks first
        // We parse Implicit first as implicit relies on the relative position
        // of the tags to determine which is the implicit parameters
        if (this.config.isImplicitMode()) {
            parameterString = this
                    .parseImplicit(parameterString, parameterList);
        }

        // Parse Explicit Long First
        parameterString = this.parseExplicit(parameterString, parameterList,
                this.explicitParameterPattern,
                this.explicitParameterPayloadPattern);
        this.logger.debug("Parameters after parsing explicit long: String("
                + parameterString + ")");

        // Parse Explicit Short Next if applicable
        if (this.config.isExplicitShortMode()) {
            parameterString = this.parseExplicit(parameterString,
                    parameterList, this.explicitShortParameterPattern,
                    this.explicitShortParameterPayloadPattern);
            this.logger
            .debug("Parameters after parsing explicit short: String("
                    + parameterString + ")");
        }

        if (!parameterString.equals("")) {
            throw new ParseException(parameterString
                    + "Parameters contains illegal syntax.");
        }

        ParameterContainer parameterContainer = new ParameterContainer(
                parameterList);
        return parameterContainer;
    }

    /**
     * Unescapes the parameter payload and returns the unescaped pattern.
     *
     * @param parameterPayload
     *            parameter to unescape.
     * @return unescaped payload.
     */
    public String unescape(String parameterPayload) {

        // unescape all the characters inside the payload
        for (Character eaParameterInMap : this.parameterLookup.keySet()) {
            parameterPayload = parameterPayload.replaceAll(
                    "\\\\" + Pattern.quote(eaParameterInMap.toString()),
                    eaParameterInMap.toString());
        }

        return parameterPayload;
    }

    private List<String> getAllMatches(Matcher matcher) {
        List<String> allMatches = new ArrayList<>();
        while (matcher.find()) {
            allMatches.add(matcher.group());
        }
        return allMatches;
    }

    /**
     * @param parameterString
     * @param parameterList
     * @param parameterPattern
     * @param parameterPayloadPattern
     * @return
     */
    private String parseExplicit(String parameterString,
            List<Parameter> parameterList, Pattern parameterPattern,
            Pattern parameterPayloadPattern) {

        Matcher parameterMatcher = parameterPattern.matcher(parameterString);

        while (parameterMatcher.find()) {

            // Get the matching group and trim away whitespace in-front if it
            // exist
            String eaParameter = parameterMatcher.group().trim();
            Character eaParameterSymbol = eaParameter.charAt(0);

            // Matches the payload of the parameter
            Matcher explicitParameterPayloadMatcher = parameterPayloadPattern
                    .matcher(eaParameter);

            // There will always be 2 groups: the symbol and its payload.
            if (explicitParameterPayloadMatcher.find()
                    && (explicitParameterPayloadMatcher.groupCount() == 0)) {

                String eaParameterPayload = explicitParameterPayloadMatcher
                        .group();

                parameterList.add(createParameter(
                        this.parameterLookup.get(eaParameterSymbol),
                        eaParameterPayload));
            } else {
                throw new RuntimeException("Payload should exist");
            }

        }
        return parameterMatcher.replaceAll("");
    }

    private String parseImplicit(String parameterString,
            List<Parameter> parameterList) throws ParseException {

        this.logger.debug("String to be parsed in Implicit Parsing:"
                + parameterString);

        Matcher implicitTimedTaskMatcher = this.implicitTimedTaskPattern
                .matcher(parameterString);
        String currentImplicitTimedTaskMatch = null;
        List<String> implicitTimedTaskMatches = this
                .getAllMatches(implicitTimedTaskMatcher);
        for (String eaTimedTaskParameterMatch : implicitTimedTaskMatches) {

            boolean isFromDateValid = true;
            boolean isToDateValid = true;
            StartTimeParameter eaStartTimeParameter = null;
            DeadlineParameter eaDeadlineParameter = null;

            // remove from keyword
            String parameterWithoutFromKeyword = eaTimedTaskParameterMatch
                    .replaceAll(TIMED_TASK_FROM_KEYWORD, "");
            String[] fromToArray = parameterWithoutFromKeyword
                    .split(TIMED_TASK_TO_KEYWORD);

            try {
                eaStartTimeParameter = new StartTimeParameter(fromToArray[0]);
            } catch (InvalidParameterException ipe) {
                isFromDateValid = false;
            }
            try {
                eaDeadlineParameter = new DeadlineParameter(fromToArray[1]);
            } catch (InvalidParameterException ipe) {
                isToDateValid = false;
            }

            if (isFromDateValid != isToDateValid) {
                throw new ParseException(
                        "Both enties in the from ... to ... must be valid dates.");
            } else {

                if (isFromDateValid == true) {
                    if (currentImplicitTimedTaskMatch == null) {
                        // both true
                        parameterList.add(eaStartTimeParameter);
                        parameterList.add(eaDeadlineParameter);
                        currentImplicitTimedTaskMatch = eaTimedTaskParameterMatch;
                    } else {
                        throw new ParseException(
                                "Can only have one from ... to ... construct");
                    }
                }
            }

        }
        if (currentImplicitTimedTaskMatch != null) {
            // replaces the parsed item to be removed
            parameterString = parameterString.replaceAll(
                    currentImplicitTimedTaskMatch, "");
        }

        Matcher implicitDeadlinedTaskMatcher = this.implicitDeadlinedTaskPattern
                .matcher(parameterString);
        List<String> implicitDeadlinedTaskMatches = this
                .getAllMatches(implicitDeadlinedTaskMatcher);
        switch (implicitDeadlinedTaskMatches.size()) {
            case 0 :
                // nothing to do, may be correct behavior
                break;
            case 1 :
                // one match
                String deadlinedTaskParameterMatch = implicitDeadlinedTaskMatches
                        .get(0);

                // remove from keyword
                deadlinedTaskParameterMatch = deadlinedTaskParameterMatch
                        .replaceAll(DEADLINED_TASK_TO_KEYWORD, "");

                parameterList.add(new DeadlineParameter(
                        deadlinedTaskParameterMatch));

                // replaces the parsed item to be removed
                parameterString = implicitDeadlinedTaskMatcher.replaceAll("");
                break;
            default :
                // all others are illegal
                throw new ParseException("Can only have one due ... construct");
        }

        new StringBuilder();
        Matcher implicitTitleMatcher = this.implicitTitlePattern
                .matcher(parameterString);

        int endIndex = -1;
        int firstStartIndex = -1;
        while (implicitTitleMatcher.find()) {
            endIndex = implicitTitleMatcher.end();
            if (firstStartIndex == -1) {
                firstStartIndex = implicitTitleMatcher.start();
            }
        }
        if (firstStartIndex != -1) {
            String titleParameterMatch = parameterString.substring(
                    firstStartIndex, endIndex).trim();
            if (!titleParameterMatch.isEmpty()) {
                parameterList.add(new TitleParameter(titleParameterMatch));
                parameterString = parameterString.replaceAll(
                        this.implicitTitlePattern.pattern(), "");
            }
        }

        this.logger.debug("After Implicit Parsing: Parameters(" + parameterList
                + "), String:\"" + parameterString + "\"");
        return parameterString;
    }
}
