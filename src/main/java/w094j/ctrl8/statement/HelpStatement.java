package w094j.ctrl8.statement;

import w094j.ctrl8.parse.CommandParser;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.terminal.Terminal;

//@author A0112521B
/**
 */
public class HelpStatement extends Statement {

    private static CommandParser commandParser = Parser.getInstance()
            .getStatementParser().getCommandParser();

    private CommandType command;

    /**
     * @param statementString
     */
    public HelpStatement(String statementString) {
        super(CommandType.HELP, statementString);
        this.command = commandParser.parse(this.getStatementArgumentsOnly());
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.help(this.command);
    }

}
