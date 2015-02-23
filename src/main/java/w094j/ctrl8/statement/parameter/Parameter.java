package w094j.ctrl8.statement.parameter;

/**
 * Represents a parameter, its symbol with its payload.
 *
 * @author Han Liang Wee, Eric(A0065517A)
 */
public class Parameter {

    private String payload;
    private ParameterSymbol symbol;

    /**
     * @return the payload
     */
    public String getPayload() {
        return this.payload;
    }

    /**
     * @return the symbol
     */
    public ParameterSymbol getSymbol() {
        return this.symbol;
    }

    /**
     * @param payload
     *            the payload to set
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    /**
     * @param symbol
     *            the symbol to set
     */
    public void setSymbol(ParameterSymbol symbol) {
        this.symbol = symbol;
    }

}
