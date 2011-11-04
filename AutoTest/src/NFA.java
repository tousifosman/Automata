

import java.util.Set;

public interface NFA extends FiniteAutomata {    
    /**
     * Returns the start state, in addition to any states reachable from the start state after epislon transitions.
     */
    public Set<State> startStates();
    
    /**
     * Returns the list of possible states that come out the given fromStates across a letter transition
     */
    public Set<State> transitions(Set<State> fromStates, Character letter);
    
    /**
     * Returns the list of possible states that come out the given fromState across a letter transition
     */
    public Set<State> transitions(State fromState, Character letter);
}
