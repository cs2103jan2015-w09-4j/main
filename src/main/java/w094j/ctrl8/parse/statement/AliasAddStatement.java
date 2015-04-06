package w094j.ctrl8.parse.statement;

import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.exception.CommandExecuteException;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112521B
public class AliasAddStatement extends Statement {
    private static Logger logger = LoggerFactory
            .getLogger(AliasAddStatement.class);

    String alias;
    String phrase;

    public AliasAddStatement(String statementString) {
        super(CommandType.ALIAS_ADD, statementString);
        if (this.getStatementArgumentsOnly().trim().split("\\s+").length < 2) {
            throw new InvalidParameterException(
                    "AliasAdd statement must specify an alias and its phrase.");
        } else {
            String arr[] = this.getStatementArgumentsOnly().split(" ", 2);
            this.alias = arr[0];
            this.phrase = arr[1];
            logger.debug("Valid alias-add Command, query \"" + statementString
                    + "\"");
        }

    }

    @Override
    public void execute(ITaskManager taskManager, boolean isUndo)
            throws CommandExecuteException {
        // Statement to be added
        taskManager.aliasAdd(this.alias, this.phrase, this, isUndo);

    }

}
