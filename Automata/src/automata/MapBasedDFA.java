package automata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A HashMap based DFA. Used mainly for temporary testing before we have
 * actual table-based DFA.
 */
public class MapBasedDFA implements DFA{
    private State startState;
    private Set<Character> alphabet;
    private Set<State> finalStates;

    private HashMap<State, HashMap<Character, State>> transitions;
    
    /**
     * Must start out with a given start state.
     * @param startState The start state to begin with
     */
    public MapBasedDFA(State startState) {
        this.startState = startState;
        this.alphabet = new HashSet<Character>();
        this.finalStates = new HashSet<State>();
        this.transitions = new HashMap<State, HashMap<Character, State>>();
    }
    
    /**
     * Adds a transition to the DFA. 
     * 
     * If the character has never been seen before, the internal DFA alphabet 
     * will be expanded to include the character. If there is already a 
     * transition on the given character from the given from state, this new 
     * transition will overwrite the old one.
     * 
     * The from state does not have to already exist in the DFA, however, the 
     * transition will be useless if it does not.
     * 
     * @param fromState The state the transition goes from
     * @param letter The character the transition is over
     * @param toState  The state the transition goes to
     */
    public void addTransisition(State fromState, Character character, State toState) {
        alphabet.add(character);
        if(toState.isFinal()) finalStates.add(toState);
        
        if(transitions.containsKey(fromState)) {
            HashMap<Character, State> transitionForState = transitions.get(fromState);
            transitionForState.put(character, toState);
        } else {
            HashMap<Character, State> transitionForState = new HashMap<Character, State>();
            transitionForState.put(character, toState);
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
