package w094j.ctrl8.statement.parameter;

import w094j.ctrl8.pojo.Task;

//@author A0065517A
/**
 */
public class TitleParameter extends Parameter {

    /**
     * @param payload
     */
    public TitleParameter(String payload) {
        super(ParameterType.TITLE, payload);
    }

    @Override
    public void add(Task task) {
        task.setTitle(this.getPayload());
    }
}
