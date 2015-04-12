//@author A0110787A
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

public abstract class GUITextGenericConfig implements IStorableElement {
    public enum TextColour {
        Awesome("orange"), Black("black"), Default("black"), Invalid("");

        private String textColour;

        TextColour(String textColour) {
            this.textColour = textColour;
        }

        @Override
        public String toString() {
            return this.textColour;
        }
    };

    protected static final TextColour DEFAULT_TEXT_COLOUR = GUITextGenericConfig.TextColour.Default;
    protected static final int DEFAULT_TEXT_SIZE = 10;
    protected TextColour textColour;
    protected Integer textSize;

    public GUITextGenericConfig() {
    }

    public GUITextGenericConfig(TextColour textColour, int textSize) {
        setTextSize(textSize);
        setTextColour(textColour);
    }

    public TextColour getTextColour() {
        if (this.textColour == null) {
            return DEFAULT_TEXT_COLOUR;
        } else {
            return this.textColour;
        }
    }

    public int getTextSize() {
        if (this.textSize == null) {
            return DEFAULT_TEXT_SIZE;
        } else {
            return this.textSize;
        }
    }

    @Override
    public boolean isValid() {
        return this.textSize > 0;
    }

    public void setTextColour(TextColour newColour) {
        this.textColour = newColour;
    }

    public void setTextSize(int textSize) {
        if (textSize > 32 || textSize < 5) {
            this.textSize = DEFAULT_TEXT_SIZE;
        } else {
            this.textSize = textSize;
        }
    }

}
