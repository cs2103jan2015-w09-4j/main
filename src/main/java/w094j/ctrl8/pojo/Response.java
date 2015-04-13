//@author A0110787A
package w094j.ctrl8.pojo;

import java.util.ArrayList;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.parse.statement.CommandType;

/**
 * Response object defines what a Display Interface expects to receive after a
 * user string processed internally for producing the appropriate feedback to
 * the user. It can contain any of these properties:
 *
 * <pre>
 * A String that contains useful feedback to the user
 * A list of tasks that are to be displayed as a table to the user
 * </pre>
 *
 * If a particular property is not part of a response, it should be defined as
 * NULL
 */
public class Response {
    public ArrayList<Actions> actions;
    public AliasData alias;
    /*
     * The list of properties(variables) that a response MAY contain
     */
    public String reply;
    public String[] TaskIdList;
    public Task[] taskList;
    private CommandType commandRan;
    private Exception exception;
    private boolean isContinueExecution;

    /**
     * Default constructor. Initializes all properties to be NULL
     */
    public Response(CommandType commandRan) {
        this.commandRan = commandRan;
        this.reply = null;
        this.taskList = null;
        this.alias = null;
        this.actions = null;
        this.TaskIdList = null;
        this.isContinueExecution = true;
    }

    /**
     * @return the commandRan
     */
    public CommandType getCommandRan() {
        return this.commandRan;
    }

    /**
     * @return the exception
     */
    public Exception getException() {
        return this.exception;
    }

    /**
     * @return the isContinueExecution
     */
    public boolean isContinueExecution() {
        return this.isContinueExecution;
    }

    /**
     * @param commandRan
     *            the commandRan to set
     */
    public void setCommandRan(CommandType commandRan) {
        this.commandRan = commandRan;
    }

    /**
     * @param isContinueExecution
     *            the isContinueExecution to set
     */
    public void setContinueExecution(boolean isContinueExecution) {
        this.isContinueExecution = isContinueExecution;
    }

    /**
     * @param exception
     *            the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }
}
