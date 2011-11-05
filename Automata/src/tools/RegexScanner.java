/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import automata.Token;
import exceptions.SyntaxErrorException;

/**
 *
 * @author Paul
 */
public class RegexScanner {

    private Token currentToken;
    private Queue<Token> tokens;
    
    public RegexScanner(Map.Entry<String, LinkedList<Token>> idEntries) {
    	tokens = new LinkedList<Token>();
    	
        for (Token a : idEntries.getValue()) {
            tokens.add(a);
        }
        currentToken = new Token("null");
    }
    
    public RegexScanner (String identifier, Map<String,LinkedList<Token>> identifiers) {
        this(identifiers.get(identifier));
    }
    
    public RegexScanner(LinkedList<Token> tokens) {
        currentToken = new Token("null");
        this.tokens = tokens;
    }

    /**
     * Clears the token buffer if token matches.
     * @param token The token to be compared against.
     * @return True if tokens matched, false otherwise.
     * @throws SyntaxErrorException 
     */
    public boolean matchToken(Token token) throws SyntaxErrorException {
    	Token pollToken = tokens.poll();
		if(token.equals(pollToken)){
			//System.out.println("Matched Token: "+ pollToken.getValue());
			return true;
		}
		else {
			//System.out.println("Mis-matched token: "+ pollToken.getValue()+"\nTrying to match: "+token.getValue());
			throw new SyntaxErrorException();
		}
    }

    /**
     * Clears the token buffer if token matches.
     * @param token The token to be compared against.
     */
    
    /*public void matchToken() throws java.io.IOException {
        currentToken = currentToken.equals(new Token("null")) ? new Token("null") : nextToken();
    }*/
    
    public Token peek() {
		Token token =tokens.peek();
		if(token ==null){
			token = new Token("null", false);
		}
		//if(token.)
		return token; 
    }
    
    /*
    private Token nextToken() {
        if (!tokens.isEmpty())
            return tokens.poll();
        else
            return new Token("null");
    }
    */
    
}

