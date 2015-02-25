package w094j.ctrl8.statement;

import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.statement.parameter.ParameterContainer;
import w094j.ctrl8.statement.parameter.ParameterSymbol;
import w094j.ctrl8.terminal.Terminal;

/**
 * Class to encapsulate a modify statement. Essentially a modify statement is an
 * add statement with a query. The syntax must be in the form:
 *
 * <pre>
 * \<modify\> \<query\> \<parameters\>
 * </pre>
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public class ModifyStatement extends Statement {

    private String query;
    private Task task;

    /**
     * Initializes a modify statement, parses the query first.
     *
     * @param statementString
     * @exception InvalidParameterException
     *                if the parameters does not exist.
     */
    public ModifyStatement(String statementString) {
        super(Command.MODIFY, statementString);

        int indexOfCommandCharacters = 0;
        String bannedSymbolsRegex = ParameterSymbol.getBannedSymbolsRegex();
        Matcher m = Pattern.compile(bannedSymbolsRegex).matcher(
                this.getArgumentsString());
        // find the first command character if it exist
        if (m.find()) {
            indexOfCommandCharacters = m.start();
        } else {
            throw new InvalidParameterException(
                    "Modify statement must specify the parameter that is different.");
        }

        if (indexOfCommandCharacters == 0) {
            throw new InvalidParameterException(
                    "Modify statement must have a query.");
        }

        // from the start of the parameter string to the index of the command
        // character, not inclusive of the command character
        this.query = this.getArgumentsString().substring(0,
                indexOfCommandCharacters);

        this.task = new Task();
        ParameterContainer container = ParameterSymbol.parse(this
                .getArgumentsString().substring(indexOfCommandCharacters));
        // TODO no validation rules for the statement
        container.addAll(null, this.task);

    }

    @Override
    public void execute(Terminal terminal) throws CommandExecuteException {
        terminal.modify(this.query, this.task);
    }
}
