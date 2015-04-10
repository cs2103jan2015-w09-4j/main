//@author A0065517A
package w094j.ctrl8.parse.statement.parameter;

import w094j.ctrl8.pojo.Task;

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
