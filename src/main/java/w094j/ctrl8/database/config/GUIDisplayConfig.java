//@author A0110787A
package w094j.ctrl8.database.config;

/**
 * This is a wrapper class that contains all configuration objects relating to the GUIDisplay component.
 */
public class GUIDisplayConfig implements IConfig {
    GUITextDisplayConfig guiTextDisplayConfig = new GUITextDisplayConfig();
    GUITextInputConfig guiTextInputConfig = new GUITextInputConfig();
    
    //The one and only constructor. Produces a default configuration.
    public GUIDisplayConfig(){
        
    }
    
    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return guiTextDisplayConfig.isValid();
    }

}
