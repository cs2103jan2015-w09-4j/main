package w094j.ctrl8.statement.parameter;

import w094j.ctrl8.pojo.Task;

/**
 * @author Han Liang Wee Eric(A0065517A)
 */
public class DeadlineParameter extends ParameterDatePayload {

    /**
     * @param payload
     */
    public DeadlineParameter(String payload) {
        super(ParameterSymbol.DEADLINE, payload);
    }

    @Override
    public void add(Task task) {
        task.setEndDate(this.getDate());
    }
}
