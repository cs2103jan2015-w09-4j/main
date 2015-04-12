//@author A0112521B
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

/**
 * Configuration file for GoogleStorage.
 */
public class GoogleStorageConfig implements IStorableElement {

    private static Boolean AUTO_DOWNLOAD_AFTER_SAVE_DEFAULT = true;
    private static Boolean SYNC_WITH_GOOGLE_DEFAULT = true;
    private Boolean autoDownloadAfterSave;
    private Boolean syncWithGoogle;

    /**
     * Creates a GoogleStorage Config object
     */
    public GoogleStorageConfig() {
    }

    /**
     * @return autoDownloadAfterSave
     */
    public Boolean getAutoDownloadAfterSave() {
        if (this.autoDownloadAfterSave == null) {
            return AUTO_DOWNLOAD_AFTER_SAVE_DEFAULT;
        } else {
            return this.autoDownloadAfterSave;
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
        if (!this.syncWithGoogle && this.autoDownloadAfterSave) {
            return false;
        }
        return true;
    }

    /**
     * @param autoSyncAfterSave
     */
    public void setAutoSync(Boolean autoSyncAfterSave) {
        this.autoDownloadAfterSave = autoSyncAfterSave;
    }

    /**
     * @param syncWithGoogle
     */
    public void setSyncWithGoogle(Boolean syncWithGoogle) {
        this.syncWithGoogle = syncWithGoogle;
    }

}
