package w094j.ctrl8.parse;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.database.config.ParserConfig;
import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.statement.Statement;

/**
 * Parser that is exposed to the world.
 */
public class Parser implements IParser {

    private static Parser instance;

    private AliasParser aliasParser;
    private StatementParser statementParser;

    /**
     * Creates a parser with the config
     *
     * @param config
     * @param aliasData
     */
    public Parser(ParserConfig config, AliasData aliasData) {
        this.aliasParser = new AliasParser(config.getAlias(), aliasData);
        this.statementParser = new StatementParser(config.getStatement());
    }

    /**
     * Gets the current instance of the Parser.If there is no instance, create
     * the instance wiht a new ParserConfig
     *
     * @return the current instance.
     */
    public static Parser getInstance() {
        if (instance == null) {
            System.out.println("GET");
            instance = initInstance(new ParserConfig(), new AliasData());
        }
        return instance;
    }

    /**
     * Creates a parser with config, stores it for future use.
     *
     * @param config
     * @param aliasData
     * @return return the configured parser.
     */
    public static Parser initInstance(ParserConfig config, AliasData aliasData) {
        if (instance != null) {
            throw new RuntimeException(
                    "Cannot initialize when it was initialized.");
        } else {
            instance = new Parser(config, aliasData);
        }
        return instance;
    }

    /**
     * @return the aliasParser
     */
    public AliasParser getAliasParser() {
        return this.aliasParser;
    }

    /**
     * @return the statementParser
     */
    public StatementParser getStatementParser() {
        return this.statementParser;
    }

    @Override
    public Statement parse(String rawInput) throws ParseException,
            DataException {
        String inputWithoutAliases = this.aliasParser.replaceAllAlias(rawInput);
        return this.statementParser.parse(inputWithoutAliases);
    }
}
