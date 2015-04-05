package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

//A0112092W

/**
 * The config of the display It is a stub currently
 */
public class DisplayConfig implements IStorableElement {

    private static final boolean IS_GUI_DEFAULT = false;

    private CLIDisplayConfig CLI;
    private GUIDisplayConfig GUI;
    private Boolean isGUI;

    public DisplayConfig() {
        this.GUI = new GUIDisplayConfig();
        this.CLI = new CLIDisplayConfig();
    }

    /**
     * @return the cLI
     */
    public CLIDisplayConfig getCLI() {
        return this.CLI;
    }

    /**
     * @return the gUI
     */
    public GUIDisplayConfig getGUI() {
        return this.GUI;
    }

    /**
     * @return the isGUI
     */
    public boolean isGUI() {
        if (this.isGUI == null) {
            return IS_GUI_DEFAULT;
        } else {
            return this.isGUI;
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * @param cLI
     *            the cLI to set
     */
    public void setCLI(CLIDisplayConfig cLI) {
        this.CLI = cLI;
    }

    /**
     * @param isGUI
     *            the isGUI to set
     */
    public void setGUI(boolean isGUI) {
        this.isGUI = isGUI;
    }

    /**
     * @param gUI
     *            the gUI to set
     */
    public void setGUI(GUIDisplayConfig gUI) {
        this.GUI = gUI;
    }

}
