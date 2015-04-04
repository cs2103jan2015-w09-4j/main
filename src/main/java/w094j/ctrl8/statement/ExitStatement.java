package w094j.ctrl8.statement;

import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0065517A
/**
 * Class to encapsulate an exit statement.
 */
public class ExitStatement extends StatementNoParams {

    /**
     * Creates a new exit Statement.
     *
     * @param statementString
     */
    public ExitStatement(String statementString) {
        super(CommandType.EXIT, statementString);
    }

    @Override
    public void execute(ITaskManager taskManager) {
        taskManager.exit();
    }

}
