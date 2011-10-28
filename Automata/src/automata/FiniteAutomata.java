package automata;

import java.util.Set;

public interface FiniteAutomata {
    /**
     * The start state of this FA
     */
    public State startState();
    
    /**
     * Returns a copy of the list of all the states in the FA
     */
    public Set<State> allStates();
    
    /**
     * Returns a copy of the alphabet of the finite automata
     */
    public Set<Character> alphabet();
    
    
    /**
     * Returns a copy the set of all final states in the FA
     */
    public Set<State> finalStates(); 
}