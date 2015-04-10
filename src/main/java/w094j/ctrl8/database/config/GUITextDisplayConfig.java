//@author A0110787A
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

public class GUITextDisplayConfig extends GUITextGenericConfig implements
        IStorableElement {
    //Uses a different set of default values
    protected static final TextColour DEFAULT_TEXT_COLOUR = TextColour.Black;
    protected static final int DEFAULT_TEXT_SIZE = 12;

    GUITextDisplayConfig() {
    }

    GUITextDisplayConfig(TextColour textColour, int textSize) {
        super(textColour, textSize);
    }
}
