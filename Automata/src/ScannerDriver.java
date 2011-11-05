
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import automata.DFA;
import automata.MapBasedDFA;
import automata.State;

/**
 * Takes in a DFA then verifies that a given file contains valid tokens. 
 * 
 * It can also generate an XML-structure of the tokens, showing how the characters
 * form into compounding tokens.
 */
public class ScannerDriver {
    private DFA dfa;
    private final File file;
    private XMLBuilder builder;

    /**
     * Creates and initializes the ScannerDriver
     * @param fileName The name of the file containing all of the tokens
     * @param dfa The DFA to use while scanning
     * @throws FileNotFoundException thrown if the file given doesn't exist.
     */
    public ScannerDriver(String fileName, DFA dfa) throws FileNotFoundException {
        this(new File(fileName), dfa);
    }

    public ScannerDriver(File inputFile, DFA dfa) throws FileNotFoundException {
        this.file = inputFile;
        if (!this.file.exists()) throw new FileNotFoundException();
        this.dfa = dfa;
        builder = new XMLBuilder();
    }

    public void run() {
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                // Uses space delimeter by default, may want to change that
                // in the future.
                String word = scan.next();
                this.parse(word);
            }
        } catch (FileNotFoundException ex) {
        }
    }

    public void parse(String word) {
        builder.reset();
        //TODO debugging DFA printout
        
        State startState = dfa.startState();
        System.out.println("Start state: "+ startState.getName());
        
        
        
        HashMap<State, HashMap<Character, State>> allTransitions = ((MapBasedDFA) dfa).getTransitions();
        Set<State> allStates = dfa.allStates();
        for (State currState : allStates) {
            HashMap<Character, State> currentTransitions = allTransitions.get(currState);
            HashSet<Character> charSet = new HashSet<Character>(currentTransitions.keySet());
            for (Character c : charSet) {
                State toState = currentTransitions.get(c);
                    String transitionChar;
                    if (c == null) {
                        transitionChar = "null";
                    } else {
                        transitionChar = Character.toString(c);
                    }
                    System.out.println(currState.getName() + "---" + transitionChar + "--->" + toState.getName()); 
            }
        }
        System.out.println("Final States:");
        Set<State> finalStates = dfa.finalStates();
        for (State s : finalStates) {
            System.out.println(s.getName());
        }
        
        
        
        
        
        
        
        
        //END of debugging DFA printout;
        
        
        
        State currState = dfa.startState();
        for (char character : word.toCharArray()) {
            currState = dfa.transition(currState, character);
            
            //TODO jw ADDED 
            if(currState!=null){
            	builder.xmlize(character, currState.getTokens());
            }
        }
        builder.finalizeXML();

        if (currState.isFinal()) {
            if (!currState.getTokens().isEmpty())
                System.out.println(word + ": ACCEPT (" + currState.topToken().getValue() + ")");
            else
                System.out.println(word + ": ACCEPT");
        } else System.out.println(word + ": REJECT");
    }
}
