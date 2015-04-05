//@author A0110787A
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

public class DisplayControllerConfig implements IStorableElement {
    public GUITextDisplayConfig textDisplayConfig;
    public GUITextInputConfig textInputConfig;

    public DisplayControllerConfig() {
        this.textDisplayConfig = new GUITextDisplayConfig();
        this.textInputConfig = new GUITextInputConfig();
    }

    public DisplayControllerConfig(GUITextDisplayConfig textDisplayConfig,
            GUITextInputConfig textInputConfig) {
        this.textDisplayConfig = textDisplayConfig;
        this.textInputConfig = textInputConfig;
    }

    @Override
    public boolean isValid() {
        return true;
    }

}
