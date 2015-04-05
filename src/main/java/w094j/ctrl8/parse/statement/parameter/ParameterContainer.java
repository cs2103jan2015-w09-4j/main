package w094j.ctrl8.parse.statement.parameter;

import java.util.List;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import w094j.ctrl8.pojo.Task;

//@author A0065517A
/**
 * A Container that handles all the parameters. it will also validates the
 * allowable parameters based on some rules.
 */
public class ParameterContainer {

    private MultiMap<ParameterType, Parameter> parameterLookup;

    /**
     * Initializes the list of parameters to this container.
     *
     * @param parameterList
     */
    public ParameterContainer(List<Parameter> parameterList) {
        this.parameterLookup = new MultiValueMap<ParameterType, Parameter>();
        for (Parameter eaParameter : parameterList) {
            this.parameterLookup.put(eaParameter.getSymbol(), eaParameter);
        }
    }

    /**
     * Validates that the Parameters conform to the rules specified for the
     * calling environment. Adding the parameters to the task if it is good.
     *
     * @param rule
     *            specifying the legal set of parameters for the calling
     *            environment.
     * @param task
     *            task to add the parameters to.
     */
    public void addAll(StatementRule rule, Task task) {

        // TODO validation for rules missing

        for (Object eaParameterObj : this.parameterLookup.values()) {
            assert (eaParameterObj instanceof Parameter);
            Parameter eaParameter = (Parameter) eaParameterObj;
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
