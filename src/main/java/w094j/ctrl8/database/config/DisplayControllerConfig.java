//@author A0110787A
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

/**
 * Configures the Display Controller.
 */
public class DisplayControllerConfig implements IStorableElement {
    private GUITextDisplayConfig textDisplayConfig;
    private GUITextInputConfig textInputConfig;

    /**
     * Initializes the DisplayController.
     */
    public DisplayControllerConfig() {
        this.textDisplayConfig = new GUITextDisplayConfig();
        this.textInputConfig = new GUITextInputConfig();
    }

    /**
     * @return
     */
    public GUITextDisplayConfig getGUITextDisplayConfig() {
        return this.getGUITextDisplayConfig();
    }

    /**
     * @return
     */
    public GUITextInputConfig getGUITextInputConfig() {
        return this.getGUITextInputConfig();
    }

    @Override
    public boolean isValid() {
        return (this.textDisplayConfig == null ? true : this.textDisplayConfig
                .isValid())
                && (this.textInputConfig == null ? true : this.textInputConfig
                        .isValid());
    }

}
