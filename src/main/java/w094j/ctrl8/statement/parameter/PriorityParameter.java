package w094j.ctrl8.statement.parameter;

import java.security.InvalidParameterException;

import w094j.ctrl8.pojo.Task;

//@author A0065517A
/**
 * Priority must be a number between 0 and 10. Inclusive of both numbers.
 */
public class PriorityParameter extends Parameter {

    private int priority;

    /**
     * Creates a new Priority Parameter.
     *
     * @param payload
     *            to be parsed to 0 to 10.
     */
    public PriorityParameter(String payload) {
        super(ParameterSymbol.PRIORITY, payload);
        assert (payload != null);
        if (!payload.isEmpty()) {
            try {
                this.priority = Integer.parseInt(payload);
            } catch (NumberFormatException nfe) {
                throw new InvalidParameterException(
                        "Priority must be an integer.");
            }

            if ((this.priority < 0) || (this.priority > 10)) {
                throw new InvalidParameterException(
                        "Priority must be between and inclusive of 0 and 10. 0 <= priority <= 10");
            }
        } else {
            this.priority = 0;
        }

    }

    @Override
    public void add(Task task) {
        task.setPriority(this.priority);
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * @param priority
     *            the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

}
