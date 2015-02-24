package w094j.ctrl8.statement.parameter;

/**
 * @author Han Liang Wee Eric(A0065517A)
 */
public class ReminderParameter extends ParameterDatePayload {

    /**
     * @param payload
     */
    public ReminderParameter(String payload) {
        super(ParameterSymbol.REMINDER, payload);
    }
}
