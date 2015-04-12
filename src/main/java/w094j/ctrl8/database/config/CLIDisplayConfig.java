package w094j.ctrl8.database.config;

import java.text.SimpleDateFormat;

import w094j.ctrl8.database.IStorableElement;

public class CLIDisplayConfig implements IStorableElement {

    private String DATE_FORMAT_DEFAULT = "dd-MMM-yy hh:mm a";

    private String dateFormat;

    /**
     * @return the dateFormat
     */
    public String getDateFormat() {
        if (this.dateFormat == null) {
            return this.DATE_FORMAT_DEFAULT;
        } else {
            return this.dateFormat;
        }
    }

    @Override
    public boolean isValid() {
        if (this.dateFormat != null) {
            try {
                new SimpleDateFormat(this.dateFormat);
            } catch (IllegalArgumentException iae) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param dateFormat
     *            the dateFormat to set
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

}
