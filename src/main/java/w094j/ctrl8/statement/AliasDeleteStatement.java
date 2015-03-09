package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B
public class AliasDeleteStatement extends StatementQuery {

    private static Logger logger = LoggerFactory.getLogger(DoneStatement.class);
    private String query;

    /**
     * @param statementString
     */
    public AliasDeleteStatement(String statementString) {
        super(Command.ALIAS_DELETE, statementString);
        this.query = this.getArgumentsString();
        logger.debug("Valid alias-delete Command, query \"" + statementString
                + "\"");
    }

    @Override
    public void execute(Terminal terminal) throws CommandExecuteException {
        // TODO Link to Terminal
        //terminal.aliasDelete(this.query);
        logger.debug("aliasDelete not implemented yet.");

    }

}