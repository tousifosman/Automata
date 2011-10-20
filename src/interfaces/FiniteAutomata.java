
import java.util.List;

public interface FiniteAutomata {
    /**
     * The start state of this FA
     */
    public State startState();
    
    /**
     * Returns the list of all the states in the FA
     */
    public List<State> allStates();
    
    /**
     * Returns the alphabet of the finite automata
     */
    public List<Character> alphabet();
    
}