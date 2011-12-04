package scanner.exceptions;

/**
 * A general Exception to be called in indication of an input file failing to meet specifications.
 * @author 
 */
public class SyntaxErrorException extends Exception {

    String mistake;

    /**
     * Default constructor - initializes instance variable to unknown
     */
    public SyntaxErrorException() {
        super();             // call superclass constructor
        mistake = "Syntax mismatch";
    }

    /**
     * Constructor receives some kind of message that is saved in an instance variable.
     * @param err Error message.
     */
    public SyntaxErrorException(String err) {
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
