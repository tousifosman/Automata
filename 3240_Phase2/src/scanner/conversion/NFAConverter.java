package scanner.conversion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import scanner.automata.DFA;
import scanner.automata.MapBasedDFA;
import scanner.automata.NFA;
import scanner.automata.State;
import scanner.automata.Token;

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
                if(letter == null) continue;

                // Get all of the NFA states after the transition from the states associated
                // with the current state
                Set<State> nfaStates = dfaToNfaConversions.get(currState);
                Set<State> transitionStates = nfa.transitions(nfaStates, letter);

                // If the transition set has already been seen, just reuse the existing DFA
                // state. Otherwise, create a new DFA state for that set, associate everything
                // and add that state to the "to-be-searched-list".
                if (nfaToDfaConversions.containsKey(transitionStates)) {
                    State nextState = nfaToDfaConversions.get(transitionStates);
                    dfa.addTransition(currState, letter, nextState);
                } else {
                    State nextState;
                    if (transitionStates != null) {
                        List<Stack<Token>> allTokens = new LinkedList<Stack<Token>>();
                        for (State state : transitionStates) {
                            allTokens.add(state.getTokens());
                        }
                        Stack<Token> mergedTokens = mergeTokens(allTokens);
                        nextState = new State(mergedTokens);
                        if (anyFinal(transitionStates))
                            nextState.setFinal(true);
                    } else {
                        Stack<Token> tokens = new Stack<Token>();
                        Token alphaToken = Token.anyCharacterToken();
                        Token endAlphaToken = alphaToken.opposite();
                        
                        tokens.push(alphaToken);
                        tokens.push(endAlphaToken);
                        nextState = new State(tokens);
                    }

                    nfaToDfaConversions.put(transitionStates, nextState);
                    dfaToNfaConversions.put(nextState, transitionStates);
                    fringeStates.add(nextState);

                    dfa.addTransition(currState, letter, nextState);
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

    private Stack<Token> mergeTokens(List<Stack<Token>> allTokens) {
        /*Stack<Token> ret = new Stack<Token>();
        for(Stack<Token> tokens: allTokens) {
            ret.addAll(tokens);
        }
        
        return ret;*/
        Stack<Token> workStack = new Stack<Token>();
        
        for(Stack<Token> tokens: allTokens) {
            if(tokens.isEmpty()){
            	continue;
            }
            Token currToken = tokens.peek();
            while(!currToken.isStartToken() && !tokens.isEmpty()) {
                currToken = tokens.pop();
                if(!workStack.contains(currToken)) {
                    workStack.push(currToken);
                }
                if(!tokens.isEmpty()){
                currToken = tokens.peek();            
                }
            }
        }
        
        for(Stack<Token> tokens: allTokens) {
            while(!tokens.isEmpty()) {
                Token currToken = tokens.pop();
                if(!workStack.contains(currToken)) {
                    workStack.push(currToken);
                }
            }
        }
        
        Stack<Token> returnTokens = new Stack<Token>();
        
        while(!workStack.isEmpty()) {
            returnTokens.push(workStack.pop());
        }
                
        return returnTokens;
    }
}
