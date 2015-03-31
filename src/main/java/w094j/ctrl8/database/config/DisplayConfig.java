package w094j.ctrl8.database.config;

//@ A0112092W

/**
 * The config of the display It is a stub currently
 */
public class DisplayConfig implements IConfig {

    private CLIDisplayConfig CLI;
    private GUIDisplayConfig GUI;

    public DisplayConfig() {
        this.GUI = new GUIDisplayConfig();
        this.CLI = new CLIDisplayConfig();
    }

// public IDisplay getCLIDisplay(){
//
// }
    @Override
    public boolean isValid() {
        return true;
    }

}
