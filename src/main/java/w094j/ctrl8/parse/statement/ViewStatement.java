package w094j.ctrl8.parse.statement;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112521B
public class ViewStatement extends StatementNoParams {

    /**
     * Creates a new view Statement.
     *
     * @param statementString
     */
    public ViewStatement(String statementString) {
        super(CommandType.VIEW, statementString);
    }

    @Override
    public void execute(ITaskManager taskManager)
            throws CommandExecuteException {
        taskManager.view();
    }

}