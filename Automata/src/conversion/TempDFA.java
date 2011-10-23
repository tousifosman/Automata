package conversion;

import automata.DFA;
import automata.State;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A temporary, HashMap based DFA used for NFA2DFA conversion.
 */
public class TempDFA implements DFA{
    private State startState;
    private Set<Character> alphabet;
    private Set<State> finalStates;

    private HashMap<State, HashMap<Character, State>> transitions;
    
    public TempDFA(State startState) {
        this.startState = startState;
        this.alphabet = new HashSet<Character>();
        this.transitions = new HashMap<State, HashMap<Character, State>>();
    }
    
    public void addTransisition(State fromState, Character letter, State toState) {
        alphabet.add(letter);
        if(toState.isFinal()) finalStates.add(toState);
        
        if(transitions.containsKey(fromState)) {
            HashMap<Character, State> transitionForState = transitions.get(fromState);
            transitionForState.put(letter, toState);
        } else {
            HashMap<Character, State> transitionForState = new HashMap<Character, State>();
            transitionForState.put(letter, toState);
            transitions.put(fromState, transitionForState);
        }
    }
    
    @Override
    public State transition(State fromState, char letter) {
        return transitions.get(fromState).get(letter);
    }

    @Override
    public State startState() {
        return startState;
    }

    @Override
    public Set<State> allStates() {
        return new HashSet<State>(transitions.keySet());
    }

    @Override
    public Set<Character> alphabet() {
        return new HashSet<Character>(alphabet);
    }

    @Override
    public Set<State> finalStates() {
        return new HashSet<State>(finalStates);
    }
}
