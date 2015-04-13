package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

//@author A0112092W
/**
 * The config of the display.
 */
public class DisplayConfig implements IStorableElement {

    private static final boolean IS_GUI_DEFAULT = false;

    private CLIDisplayConfig CLI;
    private GUIDisplayConfig GUI;
    private Boolean isGUI;

    /**
     * Display Config
     */
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
        if (this.isGUI()) {
            return ((this.GUI != null) && this.GUI.isValid());
        } else {
            return ((this.CLI != null) && this.CLI.isValid());
        }
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
