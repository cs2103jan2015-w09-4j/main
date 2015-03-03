package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

public class DeleteStatement extends Statement {

    private static Logger logger = LoggerFactory.getLogger(AddStatement.class);
    private String title;

    /**
     * @param statementString
     */
    public DeleteStatement(String statementString) {
        super(Command.DELETE, statementString);
        this.title = this.getArgumentsString();
        logger.debug("Valid delete Command, query \"" + statementString + "\"");
    }

    @Override
    public void execute(Terminal terminal) throws CommandExecuteException {
        terminal.delete(this.title);
    }

}