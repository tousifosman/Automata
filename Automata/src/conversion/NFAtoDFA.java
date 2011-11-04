package conversion;

import automata.DFA;
import automata.NFA;
import automata.State;


public class NFAtoDFA {
    public static DFA dfaFromNFA(NFA nfa) {
        State.resetCount();
        NFAConverter converter = new NFAConverter(nfa);
        return converter.dfa();
    }
}
