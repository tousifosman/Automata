package exceptions;




public class SyntaxErrorException extends Exception {
	String mistake;
	//----------------------------------------------
	// Default constructor - initializes instance variable to unknown
	  public SyntaxErrorException()
	  {
	    super();             // call superclass constructor
	    mistake = "Syntax mismatch";
	  }
	  
	//-----------------------------------------------
	// Constructor receives some kind of message that is saved in an instance variable.
	  public SyntaxErrorException(String err)
	  {
	    super(err);     // call super class constructor
	    mistake = err;  // save message
	  }
	  
	//------------------------------------------------  
	// public method, callable by exception catcher. It returns the error message.
	  public String getError()
	  {
	    return mistake;
	  }
}
