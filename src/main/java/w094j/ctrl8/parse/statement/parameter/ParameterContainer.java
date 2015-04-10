//@author A0065517A
package w094j.ctrl8.parse.statement.parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.pojo.Task;

/**
 * A Container that will handle the all the different parameters, and is
 * responsible to adding all these to a task. Moreover, it will also check that
 * each parameter appears only once.
 */
public class ParameterContainer {

    private Map<ParameterType, Parameter> parameterLookup;

    /**
     * Initializes the list of parameters to this container.
     *
     * @param parameterList
     *            the list to use to initialize the container.
     * @throws ParseException
     *             if some parameter has a duplicate.
     */
    public ParameterContainer(List<Parameter> parameterList)
            throws ParseException {
        this.parameterLookup = new HashMap<ParameterType, Parameter>();
        for (Parameter eaParameter : parameterList) {
            if (this.parameterLookup.put(eaParameter.getSymbol(), eaParameter) != null) {
                throw new ParseException("Parameters cannot be repeated.");
            }
        }
    }

    /**
     * Adds all the parameters to the task specified.
     *
     * @param task
     *            task to add the parameters to.
     */
    public void addAll(Task task) {
        for (Parameter eaParameter : this.parameterLookup.values()) {
            eaParameter.add(task);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ParameterContainer) {
            ParameterContainer paramContainerObj = (ParameterContainer) obj;
            return (this.parameterLookup
                    .equals(paramContainerObj.parameterLookup));
        }
        return false;
    }

    @Override
    public String toString() {
        return this.parameterLookup.toString();
    }
}
