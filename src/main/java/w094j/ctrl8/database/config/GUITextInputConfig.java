package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;
import w094j.ctrl8.database.config.GUITextGenericConfig.TextColour;

public class GUITextInputConfig extends GUITextGenericConfig implements IStorableElement{
    
    //Uses a different default
    GUITextInputConfig(){
        this.textColour = TextColour.Black;
        this.textSize = 20;
    }
    
    GUITextInputConfig(TextColour textColour, int textSize){
        super(textColour, textSize);
    }
    
    
    @Override
    public boolean isValid() {
        return true;
    }

}
