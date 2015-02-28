package w094j.ctrl8.statement;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

public class DoneStatement extends Statement {

    private String title;

    /**
     * @param statementString
     */
    public DoneStatement(String statementString) {
        super(Command.DONE, statementString);
        this.title = statementString;
    }

    @Override
    public void execute(Terminal terminal) throws CommandExecuteException {
        terminal.done(this.title);
    }

}