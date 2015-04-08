//@author A0110787A
package w094j.ctrl8.display.gui.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FXTextFieldInputStream extends InputStream {
    byte[] contents = new byte[0];
    int pointer = 0;
    private String delimiter = "\\A";

    private Logger logger = LoggerFactory
            .getLogger(FXTextFieldInputStream.class);

    public FXTextFieldInputStream(final TextField text) {

        text.addEventHandler(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {

                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.ENTER) {
                            if(FXTextFieldInputStream.this.pointer >=FXTextFieldInputStream.this.contents.length){
                                FXTextFieldInputStream.this.pointer = 0;
                                FXTextFieldInputStream.this.contents = text.getText().getBytes();
                            } else {
                                FXTextFieldInputStream.this.contents = (FXTextFieldInputStream.this.contents.toString()+text.getText()+delimiter)
                                    .getBytes();
                            }

                            // Replace with current buffer values
                             /*
                                                  * TODO replace with proper
                                                  * delimiter
                                                  */

                            logger.debug("Added to inputstream: "
                                    + text.getText());

                            // Flush the textarea
                            text.setText(new String());
                            text.appendText(""); // Updates all listeners
                        }
                    }
                });
    };

    // Disables the default constructor
    @SuppressWarnings("unused")
    private FXTextFieldInputStream() {
    }

    @Override
    public int read() throws IOException {
        if (contents == null || pointer >= contents.length) {
            return -1;
        } else {
            return this.contents[pointer++];
        }
        
    }

}
