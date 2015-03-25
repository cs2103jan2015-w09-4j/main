package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.taskmanager.TaskManager;

//@author A0112521B
public class DoneStatement extends StatementQuery {

    private static Logger logger = LoggerFactory.getLogger(DoneStatement.class);
    private String query;

    /**
     * @param statementString
     */
    public DoneStatement(String statementString) {
        super(CommandType.DONE, statementString);
        this.query = this.getStatementArgumentsOnly();
        logger.debug("Valid done Command, query \"" + statementString + "\"");
    }

    @Override
    public void execute(TaskManager taskManager) throws CommandExecuteException {
        // TODO Link to Terminal
        // statement to be added
        taskManager.done(this.query, this);

    }

}