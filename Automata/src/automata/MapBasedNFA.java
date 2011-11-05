package automata;


import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;

/**
 * A TreeMap based NFA.
 * 
 * An epsilon transition is represented by a transition over a "null" character
 */
public class MapBasedNFA implements NFA {
    private State startState;
    private Set<Character> alphabet;
    private Set<State> finalStates;
    private TreeMap<State, TreeMap<Character, TreeSet<State>>> transitions;

    /**
     * Must start out with a given start state.
     * @param startState The start state to begin with
     */
    public MapBasedNFA(State startState) {
        this.startState = startState;
        this.alphabet = new TreeSet<Character>();
        this.finalStates = new TreeSet<State>();
        this.transitions = new TreeMap<State, TreeMap<Character, TreeSet<State>>>();
    }

    /**
     * Adds a transition to the NFA. 
     * 
     * If the character has never been seen before, the internal NFA alphabet 
     * will be expanded to include the character. If there is already a 
     * transition on the given character from the given from state, the toState
     * will be added to the set of states going from the fromState over the 
     * character.
     * 
     * The from state does not have to already exist in the DFA, however, the 
     * transition will be useless if it does not.
     * 
     * @param fromState The state the transition goes from
     * @param letter The character the transition is over
     * @param toState  The state the transition goes to
     */
    public void addTransition(State fromState, Character character, State toState) {
        alphabet.add(character);
        if (toState.isFinal()) finalStates.add(toState);

        if (transitions.containsKey(fromState)) {
            TreeMap<Character, TreeSet<State>> transitionsForState = transitions.get(fromState);

            if (transitionsForState.containsKey(character)) {
                TreeSet<State> statesOverCharacter = transitionsForState.get(character);
                statesOverCharacter.add(toState);
            } else {
                TreeSet<State> statesOverCharacter = new TreeSet<State>();
                statesOverCharacter.add(toState);
                transitionsForState.put(character, statesOverCharacter);
            }
        } else {
            TreeMap<Character, TreeSet<State>> transitionsForState = new TreeMap<Character, TreeSet<State>>();
            TreeSet<State> statesOverCharacter = new TreeSet<State>();
            statesOverCharacter.add(toState);
            transitionsForState.put(character, statesOverCharacter);
            transitions.put(fromState, transitionsForState);
        }
    }

    @Override
    public State startState() {
        return startState;
    }

    @Override
    public Set<State> allStates() {
        return new TreeSet<State>(transitions.keySet());
    }

    @Override
    public Set<Character> alphabet() {
        return new TreeSet<Character>(alphabet);
    }

    @Override
    public Set<State> finalStates() {
        return new TreeSet<State>(finalStates);
    }

    @Override
    public Set<State> startStates() {
        Set<State> startStates = new TreeSet<State>();
        State startState = this.startState();
        startStates.add(startState);

        addEpsilonTransitions(startStates, startState);

        return startStates;
    }
    
    public TreeMap<State, TreeMap<Character, TreeSet<State>>> getTransitions() {
		return transitions;
	}
    
    
    

	/**
     * A recursive helper function that adds all the states coming from a given
     * state over epsilon transitions.
     * @param set The set to add all the states to
     * @param currState The state to start from (it is assumed that this was 
     * already added to the states)
     */
    private void addEpsilonTransitions(Set<State> set, State currState) {
        if (transitions.containsKey(currState)) {
            TreeMap<Character, TreeSet<State>> transitionsForState = transitions.get(currState);
            if (transitionsForState.containsKey(null)) {
                Set<State> states = transitionsForState.get(null);
                for (State childState : states) {
                    if (!set.contains(childState)) {
                        set.add(childState);
                        addEpsilonTransitions(set, childState);
                    }
                }
            }
        }
    }

    @Override
    public Set<State> transitions(Set<State> fromStates, Character letter) {
        if(fromStates == null) return null;
        
        Set<State> returnStates = new TreeSet<State>();

        for (State currState : fromStates) {
            Set<State> currStates = transitions(currState, letter);
            if (currStates != null)
                returnStates.addAll(currStates);
        }

        if (!returnStates.isEmpty())
            return returnStates;
        else
            return null;
    }

    /**
     * Returns all the states that can occur after transitioning over a 
     * given character on a state. This also includes epsilon transitions
     * that occur after the transition.
     * 
     * If there is no such transition, null is returned.
     * 
     * @param fromState The state to look from
     * @param letter The letter to transition over
     * @return All states that occur after the transition (+epsilon trans)
     */
    @Override
    public Set<State> transitions(State fromState, Character letter) {
        if (transitions.containsKey(fromState)) {
            TreeMap<Character, TreeSet<State>> transitionsForState = transitions.get(fromState);
            if (transitionsForState.containsKey(letter)) {
                //Get all of the normally occuring states over the transition
                Set<State> returnSet = new TreeSet<State>();
                TreeSet<State> allStates = transitionsForState.get(letter);
                returnSet.addAll(allStates);

                // Add all the states occuring over epsilon transitions from the
                // transition'd states.
                for (State currState : allStates) {
                    addEpsilonTransitions(returnSet, currState);
                }

                return returnSet;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
