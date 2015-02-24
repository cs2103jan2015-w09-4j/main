package w094j.ctrl8.statement.parameter;

/**
 * @author Han Liang Wee Eric(A0065517A)
 */
public class DeadlineParameter extends ParameterDatePayload {

    /**
     * @param payload
     */
    public DeadlineParameter(String payload) {
        super(ParameterSymbol.DEADLINE, payload);
    }
}
