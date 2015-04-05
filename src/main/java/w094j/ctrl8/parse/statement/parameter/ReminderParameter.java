package w094j.ctrl8.parse.statement.parameter;

import w094j.ctrl8.pojo.Task;

//@author A0065517A
/**
 */
public class ReminderParameter extends ParameterDatePayload {

    /**
     * @param payload
     */
    public ReminderParameter(String payload) {
        super(ParameterType.REMINDER, payload);
    }

    @Override
    public void add(Task task) {
        task.setReminder(this.getDate());
    }
}
