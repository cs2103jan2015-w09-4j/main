package w094j.ctrl8.statement.parameter;

/**
 * @author Han Liang Wee Eric(A0065517A)
 */
public class StartTimeParameter extends ParameterDatePayload {

    /**
     * @param payload
     */
    public StartTimeParameter(String payload) {
        super(ParameterSymbol.START_TIME, payload);
    }
}
