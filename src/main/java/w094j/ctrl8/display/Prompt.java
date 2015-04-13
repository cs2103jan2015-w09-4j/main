package w094j.ctrl8.display;

/**
 * Represents a prompt, the question and also the function to validate and
 * parses the user's input.
 *
 * @param <T>
 *            The parsed user input type.
 */
public abstract class Prompt<T> {

    private String question;

    /**
     * Creates a Prompt with the appropriate question.
     *
     * @param question
     */
    public Prompt(String question) {
        this.question = question;
    }

    /**
     * @return the question
     */
    public String getQuestion() {
        return this.question;
    }

    /**
     * Checks if the user input is valid for the particular question and parses
     * it.
     *
     * @param input
     *            a possible input from the user.
     * @return the parsed T from the user.
     * @throws IllegalArgumentException
     *             when the input is invalid.
     */
    public abstract T isValidInput(String input)
            throws IllegalArgumentException;

}
