package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.taskmanager.TaskManager;

//@author A0112521B
public class AliasDeleteStatement extends StatementQuery {

    private static Logger logger = LoggerFactory.getLogger(DoneStatement.class);
    private String query;

    /**
     * @param statementString
     */
    public AliasDeleteStatement(String statementString) {
        super(CommandType.ALIAS_DELETE, statementString);
        this.query = this.getStatementArgumentsOnly();
        logger.debug("Valid alias-delete Command, query \"" + statementString
                + "\"");
    }

    @Override
    public void execute(TaskManager taskManager) throws CommandExecuteException {
        // TODO Link to Terminal
        try {
            taskManager.aliasDelete(this.query,this);
        } catch (DataException e) {
            e.printStackTrace();
        }

    }

}