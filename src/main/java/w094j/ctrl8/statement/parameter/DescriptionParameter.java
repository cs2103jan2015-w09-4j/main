package w094j.ctrl8.statement.parameter;

import w094j.ctrl8.pojo.Task;

/**
 * @author Han Liang Wee Eric(A0065517A)
 */
public class DescriptionParameter extends Parameter {

    /**
     * @param payload
     */
    public DescriptionParameter(String payload) {
        super(ParameterSymbol.DESCRIPTION, payload);
    }

    @Override
    public void add(Task task) {
        task.setDescription(this.getPayload());
    }
}
