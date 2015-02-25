package w094j.ctrl8.statement.parameter;

import w094j.ctrl8.pojo.Task;

/**
 * @author Han Liang Wee Eric(A0065517A)
 */
public class StartTimeParameter extends ParameterDatePayload {

    /**
     * @param payload
     */
    public StartTimeParameter(String payload) {
        super(ParameterSymbol.START_TIME, payload);
    }

    @Override
    public void add(Task task) {
        task.setStartDate(this.getDate());
    }
}
