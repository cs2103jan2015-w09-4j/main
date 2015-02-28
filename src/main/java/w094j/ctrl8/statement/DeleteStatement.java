package w094j.ctrl8.statement;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

public class DeleteStatement extends Statement {

    private String title;

    /**
     * @param statementString
     */
    public DeleteStatement(String statementString) {
        super(Command.DELETE, statementString);
        this.title = statementString;
    }

    @Override
    public void execute(Terminal terminal) throws CommandExecuteException {
        terminal.delete(this.title);
    }

}