package automata;

import java.util.Set;

public interface FiniteAutomata {
    /**
     * The start state of this FA
     */
    public State startState();
    
    /**
     * Returns the list of all the states in the FA
     */
    public Set<State> allStates();
    
    /**
     * Returns the alphabet of the finite automata
     */
    public Set<Character> alphabet();
    
    
    /**
     * Returns the set of all final states in the FA
     */
    public Set<State> finalStates(); 
}