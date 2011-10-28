package minimization;

import automata.DFA;
import automata.MapBasedDFA;
import automata.State;
import java.util.HashMap;
import java.util.Set;

/**
 *
 */
public class DFAMinimizer {
    public static DFA minimize(DFA dfa) {
        DFAMinimizer minimizer = new DFAMinimizer(dfa);
        return minimizer.minimize();
    }
    private Set<State> finalStates;
    private Set<State> nonFinalStates;
    private Set<Character> alphabet;
    private DFA originalDFA;

    private HashMap<State, Set<State>> mergedStates;
    
    private DFAMinimizer(DFA dfa) {
        finalStates = dfa.finalStates();

        nonFinalStates = dfa.allStates();
        nonFinalStates.removeAll(finalStates);

        alphabet = dfa.alphabet();

        this.originalDFA = dfa;
        
        mergedStates = new HashMap<State, Set<State>>();
    }

    private boolean isDistinguishable(State state1, State state2) {
        boolean distinguishable = false;

        //Only accept TT, FF. Not TF, FT.
        if (!(state1.isFinal() ^ state2.isFinal())) {
            // TODO : save for later
            return true;
        }
        for (Character character : alphabet) {
            State state1Trans = originalDFA.transition(state1, character);
            State state2Trans = originalDFA.transition(state2, character);

            boolean subdistinguishable = isDistinguishable(state1Trans, state2Trans);
            // TODO : save for later
            
            if(subdistinguishable) {
                // TODO : return true;
                return true;
            }
        }

        // TODO : save for later
        return false;
    }
    
    private void setMerge(State state1, State state2) {
        if(mergedStates.get(state1)!= null) {
            // TODO 
        }
    }

    private DFA minimize() {
        State[] finalStateArray = finalStates.toArray(new State[0]);

        for (int i = 0; i < finalStateArray.length; i++) {
            for (int j = i + 1; j < finalStateArray.length; j++) {
                State firstState = finalStateArray[i];
                State secondState = finalStateArray[j];

                if (isDistinguishable(firstState, secondState)) {
                    setMerge(firstState, secondState);
                }
            }
        }

        State[] nonFinalStateArray = nonFinalStates.toArray(new State[0]);
        // TODO

        MapBasedDFA  dfa = new MapBasedDFA(originalDFA.startState());
        return dfa;
        
    }
}
