//@author A0112521B
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

/**
 * Configuration file for Database.
 */
public class DatabaseConfig implements IStorableElement {
    private DiskStorageConfig diskStorage;
    private GoogleStorageConfig googleStorage;

    /**
     * Creates a Database config with empty diskStorage and googleStorage
     * configs.
     */
    public DatabaseConfig() {
        this.diskStorage = new DiskStorageConfig();
        this.googleStorage = new GoogleStorageConfig();
    }

    /**
     * @return the diskStorageConfig
     */
    public DiskStorageConfig getDiskStorageConfig() {
        return this.diskStorage;
    }

    /**
     * @return the googleStorageConfig
     */
    public GoogleStorageConfig getGoogleStorageConfig() {
        return this.googleStorage;
    }

    @Override
    public boolean isValid() {
        return this.diskStorage.isValid()
                && this.googleStorage.isValid();
    }

    /**
     * @param diskStorageConfig
     *            the command to set
     */
    public void setDiskStorageConfig(DiskStorageConfig diskStorageConfig) {
        this.diskStorage = diskStorageConfig;
    }

    /**
     * @param googleStorageConfig
     *            the googleStorageConfig to set
     */
    public void setGoogleStorageConfig(GoogleStorageConfig googleStorageConfig) {
        this.googleStorage = googleStorageConfig;
    }

}