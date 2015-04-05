//@author A0110787A
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

public class GUITextDisplayConfig extends GUITextGenericConfig implements
        IStorableElement {

    // Uses a different default
    GUITextDisplayConfig() {
        this.textColour = TextColour.Black;
        this.textSize = 12;
    }

    GUITextDisplayConfig(TextColour textColour, int textSize) {
        super(textColour, textSize);
    }

    @Override
    public boolean isValid() {
        return true;
    }

}
