package w094j.ctrl8.statement;

import w094j.ctrl8.terminal.Terminal;

//@author A0112521B

/**
 */
public class HelpStatement extends StatementNoParams {

    /**
     * Creates a new help Statement.
     *
     * @param statementString
     */
    public HelpStatement(String statementString) {
        super(Command.HELP, statementString);
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.help();
    }

}
