package automata;

import java.util.LinkedList;
import java.util.Random;
import java.util.List;

/**
 * Tokens are stored in the state in a hierarchical list. The first token is
 * the most general token applying to the State, and the last token is the 
 * most specific/smallest token. However, tokens only matter if the state is 
 * actually final.
 */
public class State {
    private boolean finalState;
    private String name;
    private int id;
    private List<String> tokens;
    
    private static int count = 0;
    
    public State() {
        this(new LinkedList<String>());
    }
    
    public State(List<String> tokens) {
        this.tokens = new LinkedList<String>();
        this.tokens.addAll(tokens);
        
        this.name = "q" + count;
        this.id = (new Random()).nextInt();
        this.finalState = false;
        
        count++;
    }
    
    public List<String> getTokens() {
        return new LinkedList(this.tokens);
    }
   
    public boolean isFinal() {
        return finalState;
    }
    
    public void setFinal(boolean finalState) {
        this.finalState = finalState;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof State && ((State)o).id == this.id);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.id;
        return hash;
    }
}