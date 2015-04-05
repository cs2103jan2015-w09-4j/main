package w094j.ctrl8.parse.statement.parameter;

import w094j.ctrl8.pojo.Task;

//@author A0065517A
/**
 */
public class StartTimeParameter extends ParameterDatePayload {

    /**
     * @param payload
     */
    public StartTimeParameter(String payload) {
        super(ParameterType.START_TIME, payload);
    }

    @Override
    public void add(Task task) {
        task.setStartDate(this.getDate());
    }
}
