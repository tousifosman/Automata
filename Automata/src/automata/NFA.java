package automata;

import java.util.List;

public interface NFA extends FiniteAutomata {    
    /**
     * Returns the start state, in addition to any states reachable from the start state after epislon transitions.
     */
    public List<State> startStates();
    
    /**
     * Returns the list of possible states that come out the given fromStates across a letter transition
     */
    public List<State> transitions(List<State> fromStates, char letter);
    
    /**
     * Returns the list of possible states that come out the given fromState across a letter transition
     */
    public List<State> transitions(State fromState, char letter);
}
