/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generateNFA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import tools.RegexScanner;
import tools.SpecFileScanner;
import automata.MapBasedNFA;
import automata.NFA;
import automata.State;
import automata.Token;

/**
 *
 * @author Paul
 */
public class FinalNFA {

    private SpecFileScanner scan;
    private Map<String, LinkedList<Token>> identifiers;
    private Map<String, NFA> miniNFAs;
    private RegexScanner scanner;
    private RecursiveDescent parser;

    public MapBasedNFA generate(String filename) throws java.io.FileNotFoundException, java.io.IOException, exceptions.SyntaxErrorException {
        scan = new SpecFileScanner(filename);
        miniNFAs = new HashMap<String, NFA>();
        identifiers = scan.identifierDefs();
        for (Map.Entry<String, LinkedList<Token>> a : identifiers.entrySet()) {
            scanner = new RegexScanner(a);
            parser = new RecursiveDescent(scanner, scan.charClasses(),a.getKey());
            

    		RecursiveDescentInterState state = parser.regex();

    		System.out.println("Final regex: "+state.getCurrentRegex());
    		HashMap<State, HashMap<Character, HashSet<State>>> allTransitions = ((MapBasedNFA)(state.getCurrentNFA())).getTransitions();
    		Set<State> allStates = state.getCurrentNFA().allStates();
    		for(State currState : allStates){
    			HashMap<Character, HashSet<State>> currentTransitions = allTransitions.get(currState);
    			HashSet<Character> charSet = new HashSet<Character>(currentTransitions.keySet());
    			for(Character c : charSet){
    				HashSet<State> toStates = currentTransitions.get(c);
    				for(State toState : toStates){
    					String transitionChar;
    					if(c==null){
    						transitionChar = "null";
    					}
    					else {
    						transitionChar = Character.toString(c);
    					}
    					System.out.println(currState.getName()+"---" + transitionChar+"--->"+toState.getName());
    				}
    			}			
    		}		
    		System.out.println("Final States:");
    		Set<State> finalStates = ((MapBasedNFA)(state.getCurrentNFA())).finalStates();
    		for(State s: finalStates){
    			System.out.println(s.getName());
    		}
             
            miniNFAs.put(a.getKey(),state.getCurrentNFA());
        }
        
        return null;
    }
    
}
