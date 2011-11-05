package generateNFA;

import automata.NFA;


/**
 * A recursive descent intermediate state consisting of a regular expression in a String and an NFA.
 * @author 
 */
public class RecursiveDescentInterState {
	private String currentRegex;
	private NFA currentNFA;
	
	public RecursiveDescentInterState(String regex, NFA NFA){
		this.currentRegex = regex;
		this.currentNFA = NFA;
	}

        /**
         * Reads out the current regular expression for this intermediate state.
         * @return Regular expression String.
         */
	public String getCurrentRegex() {
		return currentRegex;
	}

        /**
         * Set the regular expression to the specified String for this intermediate state.
         * @param currentRegex the new regular expression.
         */
	public void setCurrentRegex(String currentRegex) {
		this.currentRegex = currentRegex;
	}

        /**
         * Reads out the current NFA for this intermediate state.
         * @return the NFA.
         */
	public NFA getCurrentNFA() {
		return currentNFA;
	}

        /**
         * Sets the current NFA for this intermediate state.
         * @param currentNFA The current NFA.
         */
	public void setCurrentNFA(NFA currentNFA) {
		this.currentNFA = currentNFA;
	}
	
	
}
