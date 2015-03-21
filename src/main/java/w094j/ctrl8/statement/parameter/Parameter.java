package w094j.ctrl8.statement.parameter;

import w094j.ctrl8.pojo.Task;

//@author A0065517A
/**
 * Represents a parameter, its symbol with its payload. This class presents no
 * understanding of the payload.
 */
public abstract class Parameter {

    private String payload;
    private ParameterType symbol;

    /**
     * Creates a Parameter. The syntax accepted on the command line is
     *
     * <pre>
     * \<symbol\>{\<payload\>}
     * </pre>
     *
     * @param symbol
     * @param payload
     */
    public Parameter(ParameterType symbol, String payload) {
        this.symbol = symbol;
        this.payload = payload;
    }

    /**
     * Adds the current parameter to the task specified.
     *
     * @param task
     */
    public abstract void add(Task task);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Parameter) {
            Parameter objParameter = (Parameter) obj;
            if (objParameter.payload.equals(this.payload)
                    && objParameter.symbol.equals(this.symbol)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * @return the payload
     */
    public String getPayload() {
        return this.payload;
    }

    /**
     * @return the symbol
     */
    public ParameterType getSymbol() {
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
    public void setSymbol(ParameterType symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.symbol.toString() + "{" + this.payload + "}";
    }

}
