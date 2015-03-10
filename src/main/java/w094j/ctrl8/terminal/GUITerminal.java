package w094j.ctrl8.terminal;

import w094j.ctrl8.application.GUICore;
import w094j.ctrl8.database.Database;
import w094j.ctrl8.display.GUIDisplay;

//@author l0lificationx
/**
 * derived class modifies the original Terminal constructor to manage I/O with
 * GUI application
 */
public class GUITerminal extends Terminal implements ITerminal {
    public GUITerminal(GUICore guiCore) {
        this.display = new GUIDisplay(guiCore);

        try {
            this.database = new Database();
        } catch (Exception e) {
            this.display.outputMessage(e.getMessage());
        }
    }
}
