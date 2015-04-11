//@author A0112521B
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

/**
 * Configuration file for GoogleStorage.
 */
public class GoogleStorageConfig implements IStorableElement {

    private static Boolean AUTO_SYNC_DEFAULT = true;
    private static Boolean SYNC_WITH_GOOGLE_DEFAULT = true;
    private Boolean autoSync;
    private Boolean syncWithGoogle;

    /**
     * Creates a GoogleStorage Config object
     */
    public GoogleStorageConfig() {
    }

    /**
     * @return autoSync
     */
    public Boolean getAutoSync() {
        if (this.autoSync == null) {
            return AUTO_SYNC_DEFAULT;
        } else {
            return this.autoSync;
        }
    }

    /**
     * @return syncWithGoogle
     */
    public Boolean getSyncWithGoogle() {
        if (this.syncWithGoogle == null) {
            return SYNC_WITH_GOOGLE_DEFAULT;
        } else {
            return this.syncWithGoogle;
        }
    }

    @Override
    public boolean isValid() {
        if (!this.syncWithGoogle && this.autoSync) {
            return false;
        }
        return true;
    }

    /**
     * @param autoSync
     */
    public void setAutoSync(Boolean autoSync) {
        this.autoSync = autoSync;
    }

    /**
     * @param syncWithGoogle
     */
    public void setSyncWithGoogle(Boolean syncWithGoogle) {
        this.syncWithGoogle = syncWithGoogle;
    }

}
