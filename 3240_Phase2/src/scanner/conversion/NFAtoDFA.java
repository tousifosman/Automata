package scanner.conversion;

import scanner.automata.DFA;
import scanner.automata.NFA;
import scanner.automata.State;

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
