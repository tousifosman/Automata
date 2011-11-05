import java.util.LinkedList;
import java.util.Queue;


public class TempScanner {
	Queue<Token> queue;
	 
	
	public TempScanner(){
		queue = new LinkedList<Token>();
		//Token token = new Token("$SMALLCASE", false);
		Token token = new Token("a", false);
		queue.add(token);
		//token = new Token("(", false);
		token = new Token("b", false);
		queue.add(token);
		//token = new Token("$LETTER", false);
		token = new Token("f", false);
		queue.add(token);
		//token = new Token("|",false);
		token = new Token("[", false);
		queue.add(token);
		//token = new Token("$DIGIT", false);
		token = new Token("c", false);
		queue.add(token);
		//token = new Token(")", false);
		token = new Token("-", false);
		queue.add(token);
		token = new Token("e", false);
		queue.add(token);
		token = new Token("]", false);
		queue.add(token);
		//token= new Token("*", false);
		token = new Token("g", false);
		queue.add(token);
		token = new Token("*", false);
		queue.add(token);
		
	}
	
	public Token peek(){
		Token token =queue.peek();
		if(token ==null){
			token = new Token("null", false);
		}
		//if(token.)
		return token; 
	}
	
	public boolean matchToken(Token token) throws SyntaxErrorException{
		Token pollToken = queue.poll();
		if(token.equals(pollToken)){
			System.out.println("Matched Token: "+ pollToken.getValue());
			return true;
		}
		else {
			//System.out.println("Mis-matched token: "+ pollToken.getValue()+"\nTrying to match: "+token.getValue());
			throw new SyntaxErrorException();
		}
	}
	
			
}
