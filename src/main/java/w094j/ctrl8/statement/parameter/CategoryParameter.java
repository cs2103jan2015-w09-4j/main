package w094j.ctrl8.statement.parameter;

import w094j.ctrl8.pojo.Task;

//@author A0065517A
/**
 */
public class CategoryParameter extends Parameter {

    /**
     * @param payload
     */
    public CategoryParameter(String payload) {
        super(ParameterType.CATEGORY, payload);
    }

    @Override
    public void add(Task task) {
        task.setCategory(this.getPayload());
    }

}
