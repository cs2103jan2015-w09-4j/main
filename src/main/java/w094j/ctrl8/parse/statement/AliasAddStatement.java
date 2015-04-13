package w094j.ctrl8.parse.statement;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.pojo.Response;
import w094j.ctrl8.taskmanager.ITaskManager;

//@author A0112521B
public class AliasAddStatement extends Statement {

    private static Logger logger = LoggerFactory
            .getLogger(AliasAddStatement.class);

    private String alias;
    private String phrase;

    public AliasAddStatement(String statementString) throws ParseException {
        super(CommandType.ALIAS_ADD, statementString);
        if (this.getStatementArgumentsOnly().trim().split("\\s+").length < 2) {
            throw new ParseException(
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
    public Response execute(ITaskManager taskManager, boolean isUndo) {
        // Statement to be added
        return taskManager.aliasAdd(this.alias, this.phrase, this, isUndo);

    }

}
