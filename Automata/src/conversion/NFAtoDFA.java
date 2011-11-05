package conversion;

import automata.DFA;
import automata.NFA;
import automata.State;

/**
 * Accomplishes NFS to DFA conversion by calling NFAConverter.
 * @author 
 */
public class NFAtoDFA {
    public static DFA dfaFromNFA(NFA nfa) {
        State.resetCount();
        NFAConverter converter = new NFAConverter(nfa);
        return converter.dfa();
    }
}
