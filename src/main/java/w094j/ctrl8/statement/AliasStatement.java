package w094j.ctrl8.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.taskmanager.TaskManager;

//@author A0065517A
/**
 * Alias Statement has no parameters.
 */
public class AliasStatement extends StatementNoParams {

    private static Logger logger = LoggerFactory
            .getLogger(AliasStatement.class);

    /**
     * Creates a new alias statement.
     *
     * @param statementString
     */
    public AliasStatement(String statementString) {
        super(CommandType.ALIAS, statementString);
    }

    @Override
    public void execute(TaskManager taskManager) {
        // TODO Link to Terminal
        logger.debug("in alias");
        taskManager.alias();
    }

}
