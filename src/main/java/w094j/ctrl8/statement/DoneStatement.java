package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

public class DoneStatement extends StatementQuery {

    private static Logger logger = LoggerFactory.getLogger(DoneStatement.class);
    private String query;

    /**
     * @param statementString
     */
    public DoneStatement(String statementString) {
        super(Command.DONE, statementString);
        this.query = this.getArgumentsString();
        logger.debug("Valid done Command, query \"" + statementString + "\"");
    }

    @Override
    public void execute(Terminal terminal) throws CommandExecuteException {
        terminal.done(this.query);
    }

}