//@author A0110787A
package w094j.ctrl8.display.gui.model;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.display.gui.view.ConsoleSceneController;

public class ConsoleSceneInputStream extends InputStream {
    private ConsoleSceneController csc;
    private Logger logger = LoggerFactory
            .getLogger(ConsoleSceneInputStream.class);
    private int pointer;

    // Specifies where the start of a byte array is.
    private final int START = 0;

    public ConsoleSceneInputStream(ConsoleSceneController csc) {
        this.csc = csc;
        this.pointer = this.START; // Puts the pointer at the starting point
    }

    @Override
    public int read() throws IOException {
        // If there is no input, or pointer is already at end of string,
        // return -1
        if (this.csc.input == null) {
            this.logger.debug("read() detects null object");
            return -1;
        } else if (this.pointer == this.csc.input.length) {
            this.logger.debug("read() at end of string");
            return -1;
        }
        if (this.pointer > this.csc.input.length) {
            /*
             * Reached when pointer has went past the String length already.
             * Blocks itself until new input is received (notified by onEnter)
             */
            synchronized (this.csc) {
                try {
                    this.wait();
                    this.pointer = 0; // Reset pointer back to start
                } catch (InterruptedException e) {
                    this.logger.debug(e.getMessage());
                    return -1; // "stop" the wait to prevent fatal error
                }
            }
        }
        return this.csc.input[this.pointer++];
    }
}
