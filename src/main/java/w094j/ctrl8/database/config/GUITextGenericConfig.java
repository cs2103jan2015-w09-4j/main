//@author A0110787A
package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

/**
 * Configs the GUI text Generic.
 */
public abstract class GUITextGenericConfig implements IStorableElement {
    /**
     * Text Color
     */
    @SuppressWarnings("javadoc")
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

    /**
     *
     */
    public GUITextGenericConfig() {
    }

    /**
     * @param textColour
     * @param textSize
     */
    public GUITextGenericConfig(TextColour textColour, int textSize) {
        this.setTextSize(textSize);
        this.setTextColour(textColour);
    }

    /**
     * @return textClour
     */
    public TextColour getTextColour() {
        if (this.textColour == null) {
            return DEFAULT_TEXT_COLOUR;
        } else {
            return this.textColour;
        }
    }

    /**
     * @return textSize
     */
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

    /**
     * @param newColour
     */
    public void setTextColour(TextColour newColour) {
        this.textColour = newColour;
    }

    /**
     * @param textSize
     */
    public void setTextSize(int textSize) {
        if ((textSize > 32) || (textSize < 5)) {
            this.textSize = DEFAULT_TEXT_SIZE;
        } else {
            this.textSize = textSize;
        }
    }

}
