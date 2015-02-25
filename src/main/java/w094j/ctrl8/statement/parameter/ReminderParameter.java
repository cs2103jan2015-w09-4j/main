package w094j.ctrl8.statement.parameter;

import w094j.ctrl8.pojo.Task;

/**
 * @author Han Liang Wee Eric(A0065517A)
 */
public class ReminderParameter extends ParameterDatePayload {

    /**
     * @param payload
     */
    public ReminderParameter(String payload) {
        super(ParameterSymbol.REMINDER, payload);
    }

    @Override
    public void add(Task task) {
        task.setReminder(this.getDate());
    }
}