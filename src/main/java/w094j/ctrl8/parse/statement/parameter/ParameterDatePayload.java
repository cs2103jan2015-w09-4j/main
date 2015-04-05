package w094j.ctrl8.parse.statement.parameter;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

//@author A0065517A
/**
 * Parameter with a Date payload.
 */
public abstract class ParameterDatePayload extends Parameter {

    private Date date;

    /**
     * Intializes and parses the payload to be a date and time.
     *
     * @param symbol
     *            symbol of the calling environment.
     * @param payload
     *            payload to be parsed into date and time.
     */
    public ParameterDatePayload(ParameterType symbol, String payload) {
        super(symbol, payload);

        // Parses the date using the method specified on Natty.
        // with references to sample code at http://natty.joestelmach.com/
        List<DateGroup> groups = new Parser().parse(payload);
        if (groups.size() != 1) {
            throw new InvalidParameterException(
                    "Each tag must have a date/time.");
        }
        DateGroup group = groups.get(0);

        List<Date> dates = group.getDates();
        if (dates.size() != 1) {
            throw new InvalidParameterException(
                    "Each tag must have a date/time.");
        }

        this.date = dates.get(0);
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

}
