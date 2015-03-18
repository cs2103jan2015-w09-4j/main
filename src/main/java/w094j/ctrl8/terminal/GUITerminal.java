package w094j.ctrl8.terminal;

import w094j.ctrl8.application.GUICore;
import w094j.ctrl8.database.Database;
import w094j.ctrl8.display.GUIDisplay;
import w094j.ctrl8.pojo.Response;

//@author A0110787A
/**
 * derived class modifies the original Terminal constructor to manage I/O with
 * GUI application
 */
public class GUITerminal extends Terminal implements ITerminal {
    public GUITerminal(GUICore guiCore) {
        this.display = new GUIDisplay();

        try {
            this.database = new Database();
        } catch (Exception e) {
            Response res = new Response();
            res.reply = e.getMessage();
            this.display.updateUI(res);
        }
    }
}
