package w094j.ctrl8.pojo;

import java.util.List;

//@author A0110787A
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
	String reply;
	List<Task> taskList; // TODO implement a table in GUI to handle**

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	/**
	 * Default constructor. Initializes all properties to be NULL
	 */
	public Response() {
		this.reply = null;
		this.taskList = null;
	}
}
