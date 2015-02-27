package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.terminal.Terminal;

/**
 * Alias Statement has no parameters.
 *
 * @author Han Liang Wee, Eric(A0065517A)
 */
public class AliasStatement extends StatementNoParams {

    private static Logger logger = LoggerFactory
            .getLogger(AliasStatement.class);

    /**
     * Creates a new alias statement.
     *
     * @param statementString
     */
    public AliasStatement(String statementString) {
        super(Command.ALIAS, statementString);
    }

    @Override
    public void execute(Terminal terminal) {
        // TODO Link to Terminal
        logger.debug("Alias command not implemented yet.");
    }

}
