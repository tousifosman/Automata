package conversion;

import automata.DFA;
import automata.NFA;


public class NFAtoDFA {
    public static DFA dfaFromNFA(NFA nfa) {
        NFAConverter converter = new NFAConverter(nfa);
        return converter.dfa();
    }
}
