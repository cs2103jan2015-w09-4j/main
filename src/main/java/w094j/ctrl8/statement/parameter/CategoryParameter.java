package w094j.ctrl8.statement.parameter;

import w094j.ctrl8.pojo.Task;

/**
 * @author Han Liang Wee Eric(A0065517A)
 */
public class CategoryParameter extends Parameter {

    /**
     * @param payload
     */
    public CategoryParameter(String payload) {
        super(ParameterSymbol.CATEGORY, payload);
    }

    @Override
    public void add(Task task) {
        task.setCategory(this.getPayload());
    }

}
