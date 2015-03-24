package w094j.ctrl8.database.config;

//@author A0112521B
/**
 * Configuration file for Database.
 */
public class DatabaseConfig implements IConfig {

    public enum Frequency {
        DAILY, MONTHLY, NEVER, SESSION, WEEKLY, YEARLY
    }

    private static Frequency AUTO_CLEAR_HISTORY_FREQUENCY_DEFAULT = Frequency.SESSION;
    private Frequency autoClearHistoryFrequency;

    public DatabaseConfig() {
    }

    /**
     * Gets how frequent the history will be cleared
     *
     * @return Frequency
     */
    public Frequency getAutoClearHistoryFrequency() {
        if (this.autoClearHistoryFrequency == null) {
            return AUTO_CLEAR_HISTORY_FREQUENCY_DEFAULT;
        } else {
            return this.autoClearHistoryFrequency;
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * Sets how frequent the history will be cleared
     *
     * @param frequency
     */
    public void setAutoClearHistoryFrequency(Frequency frequency) {
        this.autoClearHistoryFrequency = frequency;
    }

}
