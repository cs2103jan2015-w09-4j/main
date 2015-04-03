//@author A0110787A
package w094j.ctrl8.application.model;

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
    byte[] contents;
    int pointer = 0;
    private String delimiter = "\\A";

    private Logger logger = LoggerFactory
            .getLogger(FXTextFieldInputStream.class);
    private Semaphore mutex;

    public FXTextFieldInputStream(final TextField text) {
        this.mutex = new Semaphore(1);

        text.addEventHandler(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {

                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.ENTER) {

                            // Replace with current buffer values
                            contents = (text.getText().concat(delimiter))
                                    .getBytes(); /*
                                                  * TODO replace with proper
                                                  * delimiter
                                                  */
                            pointer = 0;

                            logger.debug("Updated inputstream with: "
                                    + text.getText());

                            // Flush the textarea
                            text.setText(new String());
                            text.appendText(""); // Updates all listeners

                            mutex.release();

                        }
                    }
                });

        try {
            this.mutex.acquire();
        } catch (InterruptedException e) {
            logger.info("InputStream blocked (initially)");
        }
    };

    // Disables the default constructor
    @SuppressWarnings("unused")
    private FXTextFieldInputStream() {
    }

    @Override
    public int read() throws IOException {
        if (contents == null || pointer >= contents.length) {
            try {
                mutex.acquire();
            } catch (InterruptedException e) {
                logger.debug("Acquired mutex");
            }
        } else {
            // Do not lock
        }
        return this.contents[pointer++];
    }

}
