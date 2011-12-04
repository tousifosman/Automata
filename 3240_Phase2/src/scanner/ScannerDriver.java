package scanner;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import scanner.automata.DFA;

/**
 * Takes in a DFA then verifies that a given file contains valid tokens. 
 * 
 * It can also generate an XML-structure of the tokens, showing how the characters
 * form into compounding tokens.
 */
public class ScannerDriver {

    private DFA dfa;
    private final File file;
    private int line;

    /**
     * Creates and initializes the ScannerDriver
     * @param fileName The name of the file containing all of the tokens
     * @param dfa The DFA to use while scanning
     * @throws FileNotFoundException thrown if the file given doesn't exist.
     */
    public ScannerDriver(String fileName, DFA dfa) throws FileNotFoundException {
        this(new File(fileName), dfa);
    }

    /**
     * Creates and initializes the ScannerDriver
     * @param inputFile The File containing all of the tokens
     * @param dfa The DFA to use while scanning
     * @throws FileNotFoundException thrown if the file given doesn't exist.
     */
    public ScannerDriver(File inputFile, DFA dfa) throws FileNotFoundException {
        this.file = inputFile;
        if (!this.file.exists()) {
            throw new FileNotFoundException();
        }
        this.dfa = dfa;
    }

    /**
     * Initiates the scan process.
     */
    public void run() {
        try {
            line = 0;
            Scanner scan = new Scanner(file).useDelimiter("\n");
            while (scan.hasNext()) {
                line++;
                String word = scan.next();
                this.parse(word);
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            System.out.println("Unable to write output.");        }
    }

    public void parse(String word) {

//        State currState = dfa.startState();
//        StringBuilder currToken = new StringBuilder();
//        for (char character : word.toCharArray()) {
//            currState = dfa.transition(currState, character);
//            currToken.append(character);
//            if(currState != null && currState.isFinal()) {
//                newToken(currToken.toString());
//                currToken = new StringBuilder();
//                currState = dfa.startState();
//                
//            }
//        }
//        if (currState != null) {
//            if (currState.isFinal()) {
//                if (!currState.getTokens().isEmpty()) {
//                    System.out.println(word + ": ACCEPT (" + currState.topToken().getValue() + ")");
//                    
//                } else {
//                    System.out.println(word + ": ACCEPT");
//                }
//                newToken(currToken.toString());
//            } else {
//                error();
//            }
//        } else {
//            error();
//        }
    }

    public void error() {
        System.out.println("Error at line " + line);
    }
    
    private String[] keywords = {"begin", "end",  "=",  "replace", "with", "in", ";", "recursivereplace",  ">!",  "print", "(", ")",  ",",  "#", "find", "diff",   "union", "inters", "maxfreqstring"};
    private List<String> keywordList = Arrays.asList(keywords);
    
    public void newToken(String token) {
        ScriptScanner.tokens.add(token);
        switch(token.charAt(0)) {
            case '\'':
                ScriptScanner.regexes.add(token);
                break;
            case '\"':
                ScriptScanner.strconsts.add(token);
                break;
            default:
                if (!keywordList.contains(token)){
                    ScriptScanner.identifiers.add(token);
                }
        }
    }
}
