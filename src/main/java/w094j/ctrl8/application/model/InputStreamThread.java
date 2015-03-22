package w094j.ctrl8.application.model;

import java.io.IOException;
import java.io.InputStream;

import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.application.view.ConsoleSceneController;

public class InputStreamThread extends Thread {
    private byte[] contents;
    private ConsoleSceneController csc;
    private InputStream isStream = new InputStream() {
        private int pointer = 1;

        @Override
        public int read() throws IOException {
            if (this.pointer == InputStreamThread.this.contents.length) {
                // Reached end of string
                this.pointer++;
                InputStreamThread.this.logger.debug("Reached end of String");
                return -1;
            } else if (this.pointer > InputStreamThread.this.contents.length) {
                synchronized (InputStreamThread.this.isStream) {
                    try {
                        this.wait();
                        InputStreamThread.this.logger.debug("Exit wait");
                        this.pointer = 0; // start once again from front
                    } catch (InterruptedException e) {
                        logger.debug(e.getMessage());
                        return -1;
                    }
                }
            }
            return InputStreamThread.this.contents[this.pointer];
        }
    };;
    private Logger logger = LoggerFactory.getLogger(InputStreamThread.class);
    private TextField textInput;

    public InputStreamThread(TextField textInput, ConsoleSceneController csc) {
        this.textInput = textInput;
        this.contents = this.textInput.getText().getBytes();
        this.csc = csc;
    }

    public InputStream getInputStream() {
        return this.isStream;
    }

    @Override
    public void run() {
        synchronized (this.csc) {

            this.contents = (this.textInput.getText() + "\r\n").getBytes();

            this.logger
                    .debug("Received input as: " + new String(this.contents));
            // Tells the stream to pick up the new data
            synchronized (this.isStream) {
                this.isStream.notifyAll();
            }
        }
    }
}
