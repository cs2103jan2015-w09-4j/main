//@author A0112521B
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

/**
 * Configuration file for DiskStorage.
 */
public class DiskStorageConfig implements IStorableElement {

    public enum Frequency {
        DAILY, MONTHLY, SESSION, WEEKLY, YEARLY
    }

    private static Frequency AUTO_CLEAR_HISTORY_FREQUENCY_DEFAULT = Frequency.SESSION;
    private Frequency autoClearHistoryFrequency;

    /**
     *
     */
    public DiskStorageConfig() {
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
     * Sets how frequent the history will be cleared. Possible values are:
     * Frequency.SESSION, Frequency.DAILY, Frequency.WEEKLY, Frequency.MONTHLY,
     * Frequency.YEARLY
     *
     * @param frequency
     */
    public void setAutoClearHistoryFrequency(Frequency frequency) {
        this.autoClearHistoryFrequency = frequency;
    }
}
