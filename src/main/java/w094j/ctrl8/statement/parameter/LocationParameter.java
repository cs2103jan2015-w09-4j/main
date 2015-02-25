package w094j.ctrl8.statement.parameter;

import w094j.ctrl8.pojo.Task;

/**
 * @author Han Liang Wee Eric(A0065517A)
 */
public class LocationParameter extends Parameter {

    /**
     * @param payload
     */
    public LocationParameter(String payload) {
        super(ParameterSymbol.LOCATION, payload);
    }

    @Override
    public void add(Task task) {
        task.setLocation(this.getPayload());
    }
}
