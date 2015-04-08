package w094j.ctrl8.parse.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.CommandParser;
import w094j.ctrl8.parse.Parser;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112521B
/**
 */
public class HelpStatement extends Statement {
    private static Logger logger = LoggerFactory.getLogger(HelpStatement.class);

    private CommandType command;

    /**
     * @param statementString
     */
    public HelpStatement(String statementString) {
        super(CommandType.HELP, statementString);
        CommandParser commandParser = Parser.getInstance().getStatementParser()
                .getCommandParser();
        try {

            this.command = commandParser.parse(this.getStatementArgumentsOnly()
                    .trim());
            logger.info("Help Command=" + this.command);
        } catch (ParseException e) {
            this.command = CommandType.HELP;
        }
    }

    @Override
    public void execute(ITaskManager taskManager, boolean isUndo) {
        taskManager.help(this.command);
    }

}
