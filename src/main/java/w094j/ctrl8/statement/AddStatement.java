package w094j.ctrl8.statement;

import java.security.InvalidParameterException;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.quartz.CronTrigger;

import w094j.ctrl8.message.ErrorMessage;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.terminal.Terminal;

/**
 * Class to encapsulate an add statement. Add statements must be matchable to
 * the following regex: ^add\s[A-Za-z0-9]+$ .
 *
 * @author Han Liang Wee Eric(A0065517A)
 */
public class AddStatement extends Statement {

    private CronTrigger cronString;
    private long durationInSeconds;
    private String taskName;

    /**
     * Initializes an add statement, ensures that the add statement is a valid
     * statement, the task that is added must either be:
     *
     * <pre>
     * 1. Floating Task
     * 2. Deadline Task
     * 3. Timed Task(Event)
     * </pre>
     *
     * Each would also have the option to specify a recursion.
     *
     * @param command
     *            add command.
     * @param arguments
     *            the string to be added.
     * @exception InvalidParameterException
     *                if the parameters does not exist.
     */
    public AddStatement(Command command, String arguments)
            throws InvalidParameterException {
        super(command, arguments);
        assert (Command.ADD == command);
        if (!hasParameters(arguments)) {
            throw new InvalidParameterException(
                    ErrorMessage.STATEMENT_ADD_PARAMETER);
        }

        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("deadLine")
                .withDescription("Cron Dateline.").hasArg().create("D"));
        options.addOption(OptionBuilder.withLongOpt("length")
                .withDescription("Length of task.").hasArg().create("L"));
        options.addOption(OptionBuilder.withLongOpt("name")
                .withDescription("Task Name.").hasArg().create("N"));

        // create the parser
        CommandLineParser parser = new GnuParser();

        StringTokenizer tokenizer = new StringTokenizer(arguments);
        String[] result = new String[tokenizer.countTokens()];

        try {
            // parse the command line arguments
// CommandLine line = parser.parse(options,
// matchList.toArray(new String[matchList.size()]));
            CommandLine line = parser.parse(options, result);
            System.out.println(line.getOptionValue("deadLine"));
            System.out.println(line.getOptionValue("length"));
            System.out.println(line.getOptionValue("name"));
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

    }

    @Override
    public void execute(Terminal terminal) {
        terminal.taskAdd(new Task("Test", 0, this));
    }
}
