package w094j.ctrl8.statement;

import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

/**
 */
public class HelpStatement extends Statement {

    /**
     * Creates a new help Statement.
     *
     * @param statementString
     */
    Command command;

    /**
     * @param statementString
     */
    public HelpStatement(String statementString) {
        super(Command.HELP, statementString);
        this.command = Command.parse(this.getArgumentsString());
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.help(this.command);
    }

}
