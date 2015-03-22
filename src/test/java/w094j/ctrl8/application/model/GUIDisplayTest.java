package w094j.ctrl8.application.model;

import static org.junit.Assert.fail;

import org.junit.Test;

import w094j.ctrl8.display.GUIDisplay;

public class GUIDisplayTest {

    /*
     * Tests whether application can successfully show itself.
     */
    @Test
    public void BasicDisplayTest() {
        String[] args = new String[] { "" };
        try {
            GUIDisplay guiDisplay = new GUIDisplay(args);
            assert true;
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
