//@author A0110787A
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

public class DisplayControllerConfig implements IStorableElement {
    private GUITextDisplayConfig textDisplayConfig;
    private GUITextInputConfig textInputConfig;

    public DisplayControllerConfig() {
    }

    public DisplayControllerConfig(GUITextDisplayConfig textDisplayConfig,
            GUITextInputConfig textInputConfig) {
        this.textDisplayConfig = textDisplayConfig;
        this.textInputConfig = textInputConfig;
    }
    
    public GUITextDisplayConfig getGUITextDisplayConfig(){
        if(this.textDisplayConfig==null){
            return new GUITextDisplayConfig();
        } else {
            return this.textDisplayConfig;
        }
    }
    
    public GUITextInputConfig getGUITextInputConfig(){
        if(this.textInputConfig==null){
            return new GUITextInputConfig();
        } else {
            return this.textInputConfig;
        }
    }
    
    public void setGUITextDisplayConfig(GUITextDisplayConfig config){
        this.textDisplayConfig  = config;
    }
    
    public void setGUITextInputConfig(GUITextInputConfig config){
        this.textInputConfig = config;
    }

    @Override
    public boolean isValid() {
        return (this.textDisplayConfig==null ? true : this.textDisplayConfig.isValid()) &&
               (this.textInputConfig==null ? true : this.textInputConfig.isValid());
    }

}
