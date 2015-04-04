//@author A0110787A
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

/**
 * This is a wrapper class that contains all configuration objects relating to
 * the GUIDisplay component.
 */
public class GUIDisplayConfig implements IStorableElement {
    public String[] appArgs;
    public DisplayControllerConfig controllerConfig;

    // The one and only constructor. Produces a default configuration.
    public GUIDisplayConfig() {
        this.controllerConfig = new DisplayControllerConfig();
        this.appArgs = new String[] { "" };
    }

    @Override
    public boolean isValid() {
        return (appArgs != null) && this.controllerConfig.isValid();
    }

}
