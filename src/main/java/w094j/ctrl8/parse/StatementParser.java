package w094j.ctrl8.parse;

import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.StatementConfig;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.statement.AddStatement;
import w094j.ctrl8.parse.statement.AliasAddStatement;
import w094j.ctrl8.parse.statement.AliasDeleteStatement;
import w094j.ctrl8.parse.statement.AliasStatement;
import w094j.ctrl8.parse.statement.CommandType;
import w094j.ctrl8.parse.statement.DeleteStatement;
import w094j.ctrl8.parse.statement.DoneStatement;
import w094j.ctrl8.parse.statement.ExitStatement;
import w094j.ctrl8.parse.statement.HelpStatement;
import w094j.ctrl8.parse.statement.HistoryClearStatement;
import w094j.ctrl8.parse.statement.HistoryStatement;
import w094j.ctrl8.parse.statement.HistoryUndoStatement;
import w094j.ctrl8.parse.statement.ModifyStatement;
import w094j.ctrl8.parse.statement.SearchStatement;
import w094j.ctrl8.parse.statement.Statement;
import w094j.ctrl8.parse.statement.ViewStatement;

/**
 *
 */
public class StatementParser {

    private static Logger logger = LoggerFactory
            .getLogger(StatementParser.class);

    private CommandParser commandParser;
    private ParameterParser parameterParser;

    /**
     * @param statementConfig
     */
    public StatementParser(StatementConfig statementConfig) {
        this.commandParser = new CommandParser(statementConfig.getCommand());
        this.setParameterParser(new ParameterParser(statementConfig
                .getParameter()));
    }

    /**
     * Checks arguments if it potentially contains some valid argument in it.
     *
     * @param arguments
     * @return <code>true</code> if it contains some arguments, otherwise
     *         <code>false</code>.
     */
    protected static boolean hasParameters(String arguments) {
        return !arguments.trim().equals("");
    }

    /**
     * @return the commandParser
     */
    public CommandParser getCommandParser() {
        return this.commandParser;
    }

    /**
     * @return the parameterParser
     */
    public ParameterParser getParameterParser() {
        return this.parameterParser;
    }

    /**
     * Parses a statement, the statement must not contain aliases at this time.
     *
     * @param statementString
     *            statement that is devoid of aliases
     * @return an statement object
     * @throws ParseException
     *             if there is a problem with parsing of the statement.
     */
    public Statement parse(String statementString) throws ParseException {

        // Process Alias

        CommandType command = this.commandParser.parse(statementString);
        Statement statement = null;
        logger.debug("Valid Command, parsed \"" + statementString
                + "\": Command=" + command);
        switch (command) {

            case ADD :
                statement = new AddStatement(statementString);
                break;
            case ALIAS :
                statement = new AliasStatement(statementString);
                break;
            case ALIAS_ADD :
                statement = new AliasAddStatement(statementString);
                break;
            case ALIAS_DELETE :
                statement = new AliasDeleteStatement(statementString);
                break;
            case DONE :
                statement = new DoneStatement(statementString);
                break;
            case EXIT :
                statement = new ExitStatement(statementString);
                break;
            case HISTORY :
                statement = new HistoryStatement(statementString);
                break;
            case HISTORY_CLEAR :
                statement = new HistoryClearStatement(statementString);
                break;
            case HISTORY_UNDO :
                statement = new HistoryUndoStatement(statementString);
                break;
            case MODIFY :
                statement = new ModifyStatement(statementString);
                break;
            case VIEW :
                statement = new ViewStatement(statementString);
                break;
            case HELP :
                statement = new HelpStatement(statementString);
                break;
            case DELETE :
                statement = new DeleteStatement(statementString);
                break;
            case SEARCH :
                statement = new SearchStatement(statementString);
                break;
            default :
                // should never reach here
                assert (false);
        }
        return statement;
    }

    /**
     * @param parameterParser
     *            the parameterParser to set
     */
    public void setParameterParser(ParameterParser parameterParser) {
        this.parameterParser = parameterParser;
    }

}
