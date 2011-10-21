package automata;

import automata.State;


public interface DFA extends FiniteAutomata {    
    /**
     * Returns the list of state resulting in moving from the fromState to the 
     */
    public State transition(State fromState, char letter);
    
}
