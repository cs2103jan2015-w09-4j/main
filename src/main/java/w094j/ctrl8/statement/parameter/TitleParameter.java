package w094j.ctrl8.statement.parameter;

import w094j.ctrl8.pojo.Task;

/**
 * @author Han Liang Wee Eric(A0065517A)
 */
public class TitleParameter extends Parameter {

    /**
     * @param payload
     */
    public TitleParameter(String payload) {
        super(ParameterSymbol.TITLE, payload);
    }

    @Override
    public void add(Task task) {
        task.setTaskTitle(this.getPayload());
    }
}
