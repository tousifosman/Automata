
public class RecursiveDescentInterState {
	private String currentRegex;
	private NFA currentNFA;
	
	public RecursiveDescentInterState(String regex, NFA NFA){
		this.currentRegex = regex;
		this.currentNFA = NFA;
	}

	public String getCurrentRegex() {
		return currentRegex;
	}

	public void setCurrentRegex(String currentRegex) {
		this.currentRegex = currentRegex;
	}

	public NFA getCurrentNFA() {
		return currentNFA;
	}

	public void setCurrentNFA(NFA currentNFA) {
		this.currentNFA = currentNFA;
	}
	
	
}
