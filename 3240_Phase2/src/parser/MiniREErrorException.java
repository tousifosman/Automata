package parser;

/**
 * A general Exception to be called in indication of an input file failing to meet specifications.
 * @author 
 */
public class MiniREErrorException extends Exception {

    String mistake;

    /**
     * Default constructor - initializes instance variable to unknown
     */
    public MiniREErrorException() {
        super();             // call superclass constructor
        mistake = "MiniRe Syntax mismatch";
    }

    /**
     * Constructor receives some kind of message that is saved in an instance variable.
     * @param err Error message.
     */
    public MiniREErrorException(String err) {
        super(err);     // call super class constructor
        mistake = err;  // save message
    }

    /**
     * Returns an error after being caught.
     * @return The error as a String.
     */
    public String getError() {
        return mistake;
    }
}
