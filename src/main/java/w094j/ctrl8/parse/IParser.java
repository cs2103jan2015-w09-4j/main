package w094j.ctrl8.parse;

import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.parse.statement.Statement;

/**
 * The parser that exposes all the API in parser to the world.
 */
public interface IParser {

    /**
     * Parses a statement string to a Statement object, checking for all sorts
     * of validation problems with the statement string.
     *
     * @param rawInput
     *            a statement string to parse.
     * @return the Statement object that is representative of the statement
     *         string input.
     * @throws ParseException
     *             when the input does not conform to some validation rules.
     * @throws DataException
     *             when the input causes some data operation to fail.
     */
    public Statement parse(String rawInput) throws ParseException,
    DataException;

}
