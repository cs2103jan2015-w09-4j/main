package w094j.ctrl8.database.config;

import java.text.SimpleDateFormat;

import w094j.ctrl8.database.IStorableElement;

public class CLIDisplayConfig implements IStorableElement {

    private static String APP_NAME_DEFAULT = "Ctrl-8";
    private static String DATE_FORMAT_DEFAULT = "dd-MMM-yy hh:mm a";
    private static String GOODBYE_MESSAGE_DEFAULT = "Thank you for using %1$s";
    private static String PROMPT_DEFAULT = "%1$s > ";
    private static String WELCOME_MESSAGE_DEFAULT = "Welcome for using %1$s!";

    private String appName;
    private String dateFormat;
    private String goodbyeMessage;
    private String promptDefault;
    private String welcomeMessage;

    /**
     * @return the appName
     */
    public String getAppName() {
        if (this.appName == null) {
            return APP_NAME_DEFAULT;
        } else {
            return this.appName;
        }
    }

    /**
     * @return the dateFormat
     */
    public String getDateFormat() {
        if (this.dateFormat == null) {
            return DATE_FORMAT_DEFAULT;
        } else {
            return this.dateFormat;
        }
    }

    /**
     * @return the goodbyeMessage
     */
    public String getGoodbyeMessage() {
        if (this.goodbyeMessage == null) {
            return GOODBYE_MESSAGE_DEFAULT;
        } else {
            return this.goodbyeMessage;
        }
    }

    /**
     * @return the promptDefault
     */
    public String getPromptDefault() {
        if (this.promptDefault == null) {
            return PROMPT_DEFAULT;
        } else {
            return this.promptDefault;
        }
    }

    /**
     * @return the welcomeMessage
     */
    public String getWelcomeMessage() {
        if (this.welcomeMessage == null) {
            return WELCOME_MESSAGE_DEFAULT;
        } else {
            return this.welcomeMessage;
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
     * @param appName
     *            the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @param dateFormat
     *            the dateFormat to set
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @param goodbyeMessage
     *            the goodbyeMessage to set
     */
    public void setGoodbyeMessage(String goodbyeMessage) {
        this.goodbyeMessage = goodbyeMessage;
    }

    /**
     * @param promptDefault
     *            the promptDefault to set
     */
    public void setPromptDefault(String promptDefault) {
        this.promptDefault = promptDefault;
    }

    /**
     * @param welcomeMessage
     *            the welcomeMessage to set
     */
    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

}
