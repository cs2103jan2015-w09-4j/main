package w094j.ctrl8.database.config;

import w094j.ctrl8.database.IStorableElement;

//@ A0112092W

/**
 * The config of the task manager It is currently a stub
 */
public class TaskManagerConfig implements IStorableElement {

    private DisplayConfig display;
    private ParserConfig parser;

    /**
     * @return the display
     */
    public DisplayConfig getDisplay() {
        return this.display;
    }

    /**
     * @return the parser
     */
    public ParserConfig getParser() {
        return this.parser;
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @param display
     *            the display to set
     */
    public void setDisplay(DisplayConfig display) {
        this.display = display;
    }

    /**
     * @param parser
     *            the parser to set
     */
    public void setParser(ParserConfig parser) {
        this.parser = parser;
    }

}
