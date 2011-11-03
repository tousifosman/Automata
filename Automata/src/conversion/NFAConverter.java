package conversion;

import automata.MapBasedDFA;
import automata.DFA;
import automata.NFA;
import automata.State;
import automata.Token;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * A utility class used by NFAtoDFA to perform the conversion.
 */
class NFAConverter {
    private NFA nfa;
    private MapBasedDFA dfa;

    public NFAConverter(NFA nfa) {
        this.nfa = nfa;
    }

    public NFA nfa() {
        return nfa;
    }

    public DFA dfa() {
        if (dfa == null) {
            dfa = generateDFA();
        }
        return dfa;
    }
    
    private HashMap<Set<State>, State> nfaToDfaConversions;
    private HashMap<State, Set<State>> dfaToNfaConversions;
    private Set<State> fringeStates;
    private State startState;

    private MapBasedDFA generateDFA() {
        nfaToDfaConversions = new HashMap<Set<State>, State>();
        dfaToNfaConversions = new HashMap<State, Set<State>>();
        fringeStates = new HashSet<State>();

        startState = new State();
        nfaToDfaConversions.put(nfa.startStates(), startState);
        dfaToNfaConversions.put(startState, nfa.startStates());
        fringeStates.add(startState);

        dfa = new MapBasedDFA(startState);

        completeConversion();

        return dfa;
    }

    private void completeConversion() {
        
        // While there are still DFA states we have yet to fully explore
        while (!fringeStates.isEmpty()) {
            State currState = fringeStates.toArray(new State[0])[0];
            for (Character letter : nfa.alphabet()) {
                
                // Get all of the NFA states after the transition from the states associated
                // with the current state
                Set<State> nfaStates = dfaToNfaConversions.get(currState);
                Set<State> transitionStates = nfa.transitions(nfaStates, letter);

                // If the transition set has already been seen, just reuse the existing DFA
                // state. Otherwise, create a new DFA state for that set, associate everything
                // and add that state to the "to-be-searched-list".
                if (nfaToDfaConversions.containsKey(transitionStates)) {
                    State nextState = nfaToDfaConversions.get(transitionStates);
                    dfa.addTransisition(currState, letter, nextState);
                } else {
                    Stack<Token> mergedTokens = new Stack<Token>();
                    for(State state: transitionStates) {
                        // TODO : Double check this order
                        mergedTokens.addAll(state.getTokens());
                    }
                    State nextState = new State(mergedTokens);
                    if (anyFinal(transitionStates)) nextState.setFinal(true);

                    nfaToDfaConversions.put(transitionStates, nextState);
                    dfaToNfaConversions.put(nextState, transitionStates);
                    fringeStates.add(nextState);
                    
                    dfa.addTransisition(currState, letter, nextState);
                }
            }
            fringeStates.remove(currState);
        }
    }

    private boolean anyFinal(Set<State> transitionStates) {
        for (State state : transitionStates) {
            if (state.isFinal()) return true;
        }
        return false;
    }
}
