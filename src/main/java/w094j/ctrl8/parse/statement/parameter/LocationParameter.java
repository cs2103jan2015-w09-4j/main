//@author A0065517A
package w094j.ctrl8.parse.statement.parameter;

import w094j.ctrl8.pojo.Task;

/**
 */
public class LocationParameter extends Parameter {

    /**
     * @param payload
     */
    public LocationParameter(String payload) {
        super(ParameterType.LOCATION, payload);
    }

    @Override
    public void add(Task task) {
        task.setLocation(this.getPayload());
    }
}
