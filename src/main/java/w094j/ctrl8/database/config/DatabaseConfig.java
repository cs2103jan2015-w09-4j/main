package w094j.ctrl8.database.config;

//@author A0112521B
public class DatabaseConfig implements IConfig {

    public enum Frequency {
        DAILY, MONTHLY, NEVER, SESSION, WEEKLY, YEARLY
    }

    private static Frequency AUTO_CLEAR_HISTORY_FREQUENCY_DEFAULT = Frequency.SESSION;
    private Frequency autoClearHistoryFrequency;

    public DatabaseConfig() {
        this.autoClearHistoryFrequency = AUTO_CLEAR_HISTORY_FREQUENCY_DEFAULT;
    }

    /**
     * Gets how frequent the history will be cleared
     *
     * @return Frequency
     */
    public Frequency getAutoClearHistoryFrequency() {
        return this.autoClearHistoryFrequency;
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
