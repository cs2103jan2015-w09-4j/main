//@author A0110787A
package w094j.ctrl8.pojo;

import w094j.ctrl8.statement.CommandType;

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
    /*
     * The list of properties(variables) that a response MAY contain
     */
    public String reply;
    public Task[] taskList;
    public History history;

    /**
     * Default constructor. Initializes all properties to be NULL
     */
    public Response() {
        this.reply = null;
        this.taskList = null;
        this.history = null;
    }
}
