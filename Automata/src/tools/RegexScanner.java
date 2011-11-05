/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import automata.Token;
import java.util.Map;
import java.util.Deque;
import java.util.LinkedList;

/**
 *
 * @author Paul
 */
public class RegexScanner {

    private Token currentToken;
    private Deque<Token> tokens;
    
    public RegexScanner(Map.Entry<String, LinkedList<Token>> idEntries) {
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
     */
    public boolean matchToken(Token token) {
        if (currentToken.equals(token)) {
            currentToken = new Token("");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears the token buffer if token matches.
     * @param token The token to be compared against.
     */
    public void matchToken() throws java.io.IOException {
        currentToken = currentToken.equals(new Token("null")) ? new Token("null") : nextToken();
    }
    
    public Token peek() {
        if (currentToken.equals(new Token("null"))) {
            return (currentToken = nextToken());
        } else {
            return currentToken;
        }
    }
    
    private Token nextToken() {
        if (!tokens.isEmpty())
            return tokens.pop();
        else
            return new Token("null");
    }
    
    
}

