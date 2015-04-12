//@author A0110787A
package w094j.ctrl8.display.gui.model;

import java.io.IOException;
import java.io.InputStream;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InputStream that takes a JavaFX TextField object as input, and provides a
 * means of reading it like a typical InputStream.
 */
public class FXTextFieldInputStream extends InputStream {
    byte[] contents;
    int pointer = 0;

    private Logger logger = LoggerFactory
            .getLogger(FXTextFieldInputStream.class);

    public FXTextFieldInputStream(final TextField text) {
        this.contents = new byte[] {};
        this.pointer = 1;
        text.addEventHandler(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {

                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.ENTER) {
                            FXTextFieldInputStream.this.contents = (text
                                    .getText() + "\n").getBytes();
                            FXTextFieldInputStream.this.pointer = 0;
                            text.setText("");

                            synchronized (FXTextFieldInputStream.this) {
                                FXTextFieldInputStream.this.notifyAll();
                            }
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
        if (this.pointer == this.contents.length) {
            this.pointer++;
            return -1;
        }
        if (this.pointer > this.contents.length) {
            synchronized (this) {
                try {
                    this.wait();
                    this.pointer = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        }
        return this.contents[this.pointer++];
    }

}
