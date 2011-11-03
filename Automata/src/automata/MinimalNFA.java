package automata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MinimalNFA implements NFA{
	 private Set<CharToken> tokens;
	 private State startState;
	 private Set<Character> alphabet;
	 private Set<State> finalStates;
	 //private HashMap<State, HashMap<Character, List<State>>> transitions;
	
	public MinimalNFA(){
		this.alphabet = new HashSet<Character>();
		this.finalStates = new HashSet<State>();
		this.tokens = new HashSet<CharToken>();
		//this.transitions = new HashMap<State, HashMap<Character,List<State>>>();
	}
	    
	    
	    
	    
	    
	    
	    
	    
	
	@Override
	public Set<State> startStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<State> transitions(Set<State> fromStates, Character letter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<State> transitions(State fromState, Character letter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<State> allStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Character> alphabet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<State> finalStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public State startState() {
		// TODO Auto-generated method stub
		return null;
	}

}
