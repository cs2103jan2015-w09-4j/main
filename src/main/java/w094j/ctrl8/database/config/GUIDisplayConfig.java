//@author A0110787A
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

/**
 * This is a wrapper class that contains all configuration objects relating to
 * the GUIDisplay component.
 */
public class GUIDisplayConfig implements IStorableElement {
    protected static String[] DEFAULT_ARGS = new String[] {""};
    
    protected String[] appArgs;
    protected DisplayControllerConfig controllerConfig;

    // The one and only constructor. Produces a default configuration.
    public GUIDisplayConfig() {
        this.controllerConfig = new DisplayControllerConfig();
    }
    
    public String[] getAppArgs(){
        if(this.appArgs == null){
            return DEFAULT_ARGS;
        } else {
            return this.appArgs;
        }
    }

    @Override
    public boolean isValid() {
        return (this.controllerConfig != null && this.controllerConfig.isValid());
    }

}
