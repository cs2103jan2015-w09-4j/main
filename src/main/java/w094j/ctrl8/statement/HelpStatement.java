package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.CommandParser;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.taskmanager.TaskManager;

//@author A0112521B
/**
 */
public class HelpStatement extends Statement {
    private static CommandParser commandParser = Parser.getInstance()
            .getStatementParser().getCommandParser();
    private static Logger logger = LoggerFactory.getLogger(HelpStatement.class);

    private CommandType command;

    /**
     * @param statementString
     */
    public HelpStatement(String statementString) {
        super(CommandType.HELP, statementString);
        try {
            this.command = commandParser.parse(this.getStatementArgumentsOnly()
                    .trim());
            logger.info("Help Command=" + this.command);
        } catch (ParseException e) {
            this.command = CommandType.HELP;
        }
    }

    @Override
    public void execute(TaskManager taskManager) {
        taskManager.help(this.command);
    }

}
