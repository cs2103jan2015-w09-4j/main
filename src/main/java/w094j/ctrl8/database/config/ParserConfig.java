package w094j.ctrl8.database.config;

import java.util.EnumSet;
import java.util.regex.Pattern;

import w094j.ctrl8.statement.parameter.ParameterType;

/**
 */
public class ParserConfig implements IConfig {

    /**
     * Matches only the characters defined below; basically characters that are
     * accessible from an EN-US keyboard.
     */
    static final String SYMBOL_REGEX = "[" + Pattern.quote("~")
            + Pattern.quote("`") + Pattern.quote("!") + Pattern.quote("@")
            + Pattern.quote("#") + Pattern.quote("$") + Pattern.quote("%")
            + Pattern.quote("^") + Pattern.quote("&") + Pattern.quote("*")
            + Pattern.quote("(") + Pattern.quote(")") + Pattern.quote("-")
            + Pattern.quote("_") + Pattern.quote("=") + Pattern.quote("+")
            + Pattern.quote("[") + Pattern.quote("{") + Pattern.quote("]")
            + Pattern.quote("}") + Pattern.quote("\\") + Pattern.quote("|")
            + Pattern.quote(";") + Pattern.quote(":") + Pattern.quote("'")
            + Pattern.quote("\"") + Pattern.quote(",") + Pattern.quote("<")
            + Pattern.quote(".") + Pattern.quote(">") + Pattern.quote("/")
            + Pattern.quote("?") + "]";

    /**
     * Ensures that the symbol for alias and the parameter symbols are all
     * unique; matches the symbols taken from the union of the 2 REGEX and
     * ensure that there are no duplicates symbols.
     */
    static final String UNIQUE_SYMBOLS_REGEX = "^(?:("
            + AliasConfig.ACCEPTABLE_SYMBOL_REGEX + "|"
            + ParameterConfig.ACCEPTABLE_SYMBOL_REGEX + ")(?!.*\1))*$";

    private AliasConfig alias;
    private StatementConfig statement;

    /**
     * Creates a Parser config with empty alias and statement configs.
     */
    public ParserConfig() {
        this.alias = new AliasConfig();
        this.statement = new StatementConfig();
    }

    public ParserConfig(AliasConfig alias) {
        this.alias = alias;
        this.statement = new StatementConfig();
    }
    /**
     * @return the alias
     */
    public AliasConfig getAlias() {
        return this.alias;
    }

    /**
     * @return the statement
     */
    public StatementConfig getStatement() {
        return this.statement;
    }

    @Override
    public boolean isValid() {
        // Constructs a string with all the symbols; from alias and parameters
        StringBuilder sb = new StringBuilder();
        sb.append(this.alias.getAliasCharacter());
        for (ParameterType eaParameterType : EnumSet.allOf(ParameterType.class)) {
            sb.append(this.statement.getParameter().get(eaParameterType));
        }
        // make sure the alias and parameter symbols are unique
        return sb.toString().matches(UNIQUE_SYMBOLS_REGEX)
        // then ensure that both the alias and statement configs are unique
                && this.alias.isValid() && this.statement.isValid();
    }

    /**
     * @param alias
     *            the alias to set
     */
    public void setAlias(AliasConfig alias) {
        this.alias = alias;
    }

    /**
     * @param statement
     *            the statement to set
     */
    public void setStatement(StatementConfig statement) {
        this.statement = statement;
    }
}
