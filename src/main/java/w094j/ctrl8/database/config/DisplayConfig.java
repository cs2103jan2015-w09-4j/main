package w094j.ctrl8.database.config;

import w094j.ctrl8.display.IDisplay;
//@ A0112092W

/**
 * The config of the display
 * It is a stub currently
 *
 */
public class DisplayConfig implements IConfig{

    
    private GUIDisplayConfig GUI;
    private CLIDisplayConfig CLI;
    
    public DisplayConfig(){
        this.GUI = new GUIDisplayConfig();
        this.CLI = new CLIDisplayConfig();
    }
    
    public IDisplay getCLIDisplay(){
        
    }
    @Override
    public boolean isValid() {
        return true;
    }
    

}
