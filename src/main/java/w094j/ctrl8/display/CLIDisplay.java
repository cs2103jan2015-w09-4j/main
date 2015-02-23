package w094j.ctrl8.display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import w094j.ctrl8.message.ErrorMessage;

/**
 * Class implements Display Interface as a simple CLI
 * How to use:
 * To get userinput as a String, call CLIDisplay.getUserInput()
 * To display an output, call CLIDisplay.outputMessage(message)
 */

/**
 * @author Rodson Chue Le Sheng(A0110787)
 */
public class CLIDisplay implements Display {
    private BufferedReader br;

    /**
     * Public constructor for a CLI Display
     */
    public CLIDisplay() {
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String getUserInput() {
        String nextLine = null;
        try {
            nextLine = this.br.readLine();
        } catch (IOException e) {
            this.outputMessage(ErrorMessage.ERROR_READING_INPUT);
            return null;
        }
        return nextLine;
    }

    @Override
    public void outputMessage(String message) {
        System.out.println(message);
    }

}
