package w094j.ctrl8.parse.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.ITaskManager;

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
    public Response execute(ITaskManager taskManager, boolean isUndo) {
        return taskManager.alias(this);
    }

}
