package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

/**
 *
 */
public class StatementConfig implements IStorableElement {

    private CommandConfig command;
    private ParameterConfig parameter;

    /**
     * Creates a Statement config with empty command and parameter configs.
     */
    public StatementConfig() {
        this.command = new CommandConfig();
        this.parameter = new ParameterConfig();
    }

    /**
     * @return the command
     */
    public CommandConfig getCommand() {
        return this.command;
    }

    /**
     * @return the parameter
     */
    public ParameterConfig getParameter() {
        return this.parameter;
    }

    @Override
    public boolean isValid() {
        return this.command.isValid() && this.parameter.isValid();
    }

    /**
     * @param command
     *            the command to set
     */
    public void setCommand(CommandConfig command) {
        this.command = command;
    }

    /**
     * @param parameter
     *            the parameter to set
     */
    public void setParameter(ParameterConfig parameter) {
        this.parameter = parameter;
    }
}
