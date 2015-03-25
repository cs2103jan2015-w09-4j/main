package w094j.ctrl8.database.config;

public abstract class GUITextGenericConfig implements IConfig {
    public enum TextColour{
        Black("black"), Awesome("orange"), Invalid(""), Default("black");
        
        private String textColour;
        TextColour(String textColour){
            this.textColour = textColour;
        }
        
        @Override
        public String toString(){
            return this.textColour;
        }
    };
    
    protected TextColour textColour;
    protected int textSize;
    protected static final TextColour DEFAULT_TEXT_COLOUR = GUITextGenericConfig.TextColour.Default;
    protected static final int DEFAULT_TEXT_SIZE = 10;

    @Override
    public boolean isValid() {
        return true;
    }
    
    public GUITextGenericConfig(){
        this.textColour = DEFAULT_TEXT_COLOUR;
        this.textSize = DEFAULT_TEXT_SIZE;
    }
    
    public GUITextGenericConfig(TextColour textColour, int textSize){
        this.textColour = textColour;
        this.textSize = textSize;
    }
    
    public void setTextColour(TextColour newColour){
        if(newColour == null){
            //TODO add logger
        } else if(newColour == TextColour.Default) {
            this.textColour = null;
        } else {
            this.textColour = newColour;
        }
    }
    
    //Function to allow textsize to be changed
    public void setTextSize(int textSize){
        if(textSize > 32 || textSize < 5){
            //TODO add logger
        } else {
            this.textSize = textSize;
        }
    }

}
