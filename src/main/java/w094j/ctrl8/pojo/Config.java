package w094j.ctrl8.pojo;

/**
 * Class encapsulates configuration options for program. This includes whether
 * program is running in CLI or GUI mode, where to read/dump data to/from etc.
 * TODO: support for google integration, possibly via an additional argument
 * parameter
 */
/**
 * @author Rodson Chue Le Sheng (A0110787A)
 * @author Lin Chen-Hsin A0112521B
 */
public class Config {
    /**
     * Parses arguements passed into the program and produces a config object
     * based on the arguements
     *
     * @param args
     *            The user specified arguements(if applicable)
     * @return Config object to initiate terminal
     */
    public static Config parseArgs(String[] args) {
        /*
         * TODO: Read an external file as a config file and produce a config
         * object based on it.
         */
        return null;
    }
}
