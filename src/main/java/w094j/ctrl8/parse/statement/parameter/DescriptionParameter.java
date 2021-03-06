//@author A0065517A
package w094j.ctrl8.parse.statement.parameter;

import w094j.ctrl8.pojo.Task;

/**
 */
public class DescriptionParameter extends Parameter {

    /**
     * @param payload
     */
    public DescriptionParameter(String payload) {
        super(ParameterType.DESCRIPTION, payload);
    }

    @Override
    public void add(Task task) {
        task.setDescription(this.getPayload());
    }
}
