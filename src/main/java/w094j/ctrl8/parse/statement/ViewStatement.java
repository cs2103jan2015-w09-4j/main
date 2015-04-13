package w094j.ctrl8.parse.statement;

import w094j.ctrl8.pojo.Response;
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
    public Response execute(ITaskManager taskManager, boolean isUndo) {
        return taskManager.view(this);
    }

}