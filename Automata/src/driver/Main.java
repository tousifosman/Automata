package driver;


import automata.*;
import conversion.NFAtoDFA;
import generateNFA.FinalNFA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import exceptions.SyntaxErrorException;
import minimization.DFAMinimizer;

public class Main {

    public static File directory;

    /**
     * Creates the DFA and ScannerDriver using the files containing the regexes
     * and the tokens.
     * 
     * If no arguments are supplied, the user is prompted for inputs
     * 
     * @param args 1st argument is used as the name of the file containing all 
     * of the tokens
     * @throws SyntaxErrorException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, SyntaxErrorException {
        checkArgs(args);

        DFA dfa;
        if (args.length == 0) {
            Object[] options = {"Generate DFA",
                "Use Existing DFA"};
            int n = JOptionPane.showOptionDialog(null,
                    "Would you like to generate a DFA from lexical specifications or "
                    + "use an existing DFA from export?",
                    "DFA Source",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    null);
            if (n == 0) {
                dfa = generateDFA();
            } else {
                dfa = loadDFA();
            }
        } else {
            if (args[0].equalsIgnoreCase("--createDFA") || args[0].equalsIgnoreCase("--run")) {
                dfa = generateDFA(args[1]);
            } else {
                dfa = loadDFA(args[1]);
            }
        }

        if (args.length == 0) {
            int save = JOptionPane.showConfirmDialog(null, "Do you want to save the DFA?", "DFA Exporter",
                    JOptionPane.YES_NO_OPTION);
            if (save == 0) {
                saveDFA(dfa);
            }
        } else if (args.length > 0 && args[0].equalsIgnoreCase("--createDFA")) {
            saveDFA(dfa, args[2]);
            System.exit(0);
        } else if (args.length > 3 && args[3].equalsIgnoreCase("--saveDFA")) {
            saveDFA(dfa, args[4]);
        }

        File inputFile;

        if (args.length == 0) {
            JOptionPane.showMessageDialog(null, "Next, choose the test cases to examine.");
            JFileChooser fc = new JFileChooser(directory);
            fc.showOpenDialog(null);
            inputFile = fc.getSelectedFile();
        } else {
            inputFile = new File(args[2]);
        }

        try {
            ScannerDriver driver = new ScannerDriver(inputFile, dfa);
            driver.run();
        } catch (FileNotFoundException ex) {
            System.out.println("Error: Cannot find file");
            System.out.println(ex.getMessage());
        }
    }

    private static void checkArgs(String[] args) {
        if (args.length == 0) {
            return;
        }
        if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
            System.out.println("Main --run 'specs' 'tests' --saveDFA 'dfa'");
            System.out.println(" - Takes the input specs and runs it against tests, printing results");
            System.out.println(" - --saveDFA 'dfa' is optional");
            System.out.println(" - 'specs', 'tests' and 'dfa' can be any filename");
            System.out.println();
            System.out.println("Main --createDFA 'input' 'output'");
            System.out.println(" - Takes 'input' lex. specs and saves the resulting DFA to 'output'");
            System.out.println(" - 'input' and 'output' can be any filename");
            System.out.println();
            System.out.println("Main --runDFA 'dfa' 'tests'");
            System.out.println(" - Takes the input dfa and runs it against tests, printing results");
            System.out.println(" - 'dfa' and 'tests' can be any filename");
            System.exit(0);
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("--createDFA") || args[0].equalsIgnoreCase("--runDFA") || args[0].equalsIgnoreCase("--run")) {
                return;
            }
        }
        if (args.length == 5) {
            if (args[0].equalsIgnoreCase("--run") && args[3].equalsIgnoreCase("--saveDFA")) {
                return;
            }
        }
        System.out.println("Incorrect arguments. If you don't know them run 'Main help' or use no args for GUI-mode");
        System.exit(1);
    }

    private static DFA loadDFA() {
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        File dfaFile = fc.getSelectedFile();
        directory = fc.getCurrentDirectory();
        return DFAExportImport.importDFA(dfaFile);
    }

    private static DFA loadDFA(String fileName) {
        File file = new File(fileName);
        return DFAExportImport.importDFA(file);
    }

    private static DFA generateDFA() throws FileNotFoundException, IOException, SyntaxErrorException {
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        File lexSpecs = fc.getSelectedFile();
        directory = fc.getCurrentDirectory();
        
    	FinalNFA NFAgen = new FinalNFA();
    	NFA nfa  = NFAgen.generate(lexSpecs.getAbsolutePath());
        
    	
    	System.out.println("\n\n\nCOMBINED NFA STARTS HERE\nStart State: "+nfa.startState().getName());
    	
    	
        HashMap<State, HashMap<Character, HashSet<State>>> allTransitions = ((MapBasedNFA) (nfa)).getTransitions();
        Set<State> allStates = nfa.allStates();
        for (State currState : allStates) {
            HashMap<Character, HashSet<State>> currentTransitions = allTransitions.get(currState);
            HashSet<Character> charSet = new HashSet<Character>(currentTransitions.keySet());
            for (Character c : charSet) {
                HashSet<State> toStates = currentTransitions.get(c);
                for (State toState : toStates) {
                    String transitionChar;
                    if (c == null) {
                        transitionChar = "null";
                    } else {
                        transitionChar = Character.toString(c);
                    }
                    System.out.println(currState.getName() + "---" + transitionChar + "--->" + toState.getName());
                }
            }
        }
        System.out.println("Final States:");
        Set<State> finalStates = ((MapBasedNFA) (nfa)).finalStates();
        for (State s : finalStates) {
            System.out.println(s.getName());
        }       
        DFA dfa = NFAtoDFA.dfaFromNFA(nfa);
        //dfa = DFAMinimizer.minimize(dfa);

        return dfa;
    }

    private static DFA generateDFA(String fileName) throws FileNotFoundException, IOException, SyntaxErrorException {
    	FinalNFA NFAgen = new FinalNFA();
    	NFA nfa  = NFAgen.generate(fileName);
        DFA dfa = NFAtoDFA.dfaFromNFA(nfa);
        //dfa = DFAMinimizer.minimize(dfa);

        return dfa;
    }

    private static void saveDFA(DFA dfa) {
        JFileChooser fileSaver = new JFileChooser(directory);
        fileSaver.showSaveDialog(null);
        File exportDFA = fileSaver.getSelectedFile();
        DFAExportImport.exportDFA(dfa, exportDFA);
    }

    private static void saveDFA(DFA dfa, String fileName) {
        File exportDFA = new File(fileName);
        DFAExportImport.exportDFA(dfa, exportDFA);
    }

    /**
     * A temporary DFA used for testing.
     */
    private static DFA testDFA() {
        State startState = new State();
        MapBasedDFA dfa = new MapBasedDFA(startState);

        Token start = new Token("SMALLCASE", true);
        Token end = new Token("SMALLCASE", false);
        Token overallStart = new Token("IDENTIFIER", true);

        Stack<Token> stack = new Stack<Token>();
        stack.push(overallStart);
        stack.push(start);
        stack.push(end);
        State small = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransition(startState, letter, small);
        }
        start = new Token("UPPERCASE", true);
        end = new Token("UPPERCASE", false);
        overallStart = new Token("CONSTANT", true);

        stack = new Stack<Token>();
        stack.push(overallStart);
        stack.push(start);
        stack.push(end);
        State upper = new State(stack);
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransition(startState, letter, upper);
        }
        start = new Token("DIGIT", true);
        end = new Token("DIGIT", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);

        State fail = new State(stack);
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransition(startState, letter, fail);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransition(fail, letter, fail);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransition(fail, letter, fail);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransition(fail, letter, fail);
        }
        start = new Token("LETTER", true);
        end = new Token("LETTER", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);

        State smallLetter = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransition(small, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransition(small, letter, smallLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransition(small, letter, fail);
        }

        start = new Token("LETTER", true);
        end = new Token("LETTER", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);

        State upperLetter = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransition(upper, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransition(upper, letter, upperLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransition(upper, letter, fail);
        }

        Token overallEnd = new Token("IDENTIFIER", false);
        start = new Token("DIGIT", true);
        end = new Token("DIGIT", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);
        stack.push(overallEnd);

        State smallLetterDigit = new State(stack);
        smallLetterDigit.setFinal(true);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransition(smallLetter, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransition(smallLetter, letter, smallLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransition(smallLetter, letter, smallLetterDigit);
        }
        overallEnd = new Token("CONSTANT", false);
        start = new Token("DIGIT", true);
        end = new Token("DIGIT", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);
        stack.push(overallEnd);

        State upperLetterDigit = new State(stack);
        upperLetterDigit.setFinal(true);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransition(upperLetter, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransition(upperLetter, letter, upperLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransition(upperLetter, letter, upperLetterDigit);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransition(smallLetterDigit, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransition(smallLetterDigit, letter, smallLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransition(smallLetterDigit, letter, smallLetterDigit);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransition(upperLetterDigit, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransition(upperLetterDigit, letter, upperLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransition(upperLetterDigit, letter, upperLetterDigit);
        }
        return dfa;
    }

    /**
     * A temporary NFA used for testing.
     */
    private static NFA testNFA() {
        State startState = new State();
        MapBasedNFA nfa = new MapBasedNFA(startState);

        Token start = new Token("SMALLCASE", true);
        Token end = new Token("SMALLCASE", false);
        Token overallStart = new Token("IDENTIFIER", true);

        Stack<Token> stack = new Stack<Token>();
        stack.push(overallStart);
        stack.push(start);
        stack.push(end);
        State small = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransition(startState, letter, small);
        }

        /* Special Strings for testing */
        // Accept SMALLCASE (DIGIT)*
        start = new Token("SMALLCASE", true);
        end = new Token("SMALLCASE", false);
        overallStart = new Token("SPECIAL", true);

        stack = new Stack<Token>();
        stack.push(overallStart);
        stack.push(start);
        stack.push(end);
        State small2 = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransition(startState, letter, small2);
        }

        start = new Token("DIGIT", true);
        end = new Token("DIGIT", false);
        Token overallEnd = new Token("SPECIAL", false);
        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);
        stack.push(overallEnd);
        State small2Digit = new State(stack);
        small2Digit.setFinal(true);
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransition(small2, letter, small2Digit);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransition(small2Digit, letter, small2Digit);
        }

        /* End Special Strings */

        start = new Token("UPPERCASE", true);
        end = new Token("UPPERCASE", false);
        overallStart = new Token("CONSTANT", true);

        stack = new Stack<Token>();
        stack.push(overallStart);
        stack.push(start);
        stack.push(end);
        State upper = new State(stack);
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransition(startState, letter, upper);
        }
        start = new Token("LETTER", true);
        end = new Token("LETTER", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);

        State smallLetter = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransition(small, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransition(small, letter, smallLetter);
        }

        start = new Token("LETTER", true);
        end = new Token("LETTER", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);

        State upperLetter = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransition(upper, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransition(upper, letter, upperLetter);
        }


        overallEnd = new Token("IDENTIFIER", false);
        start = new Token("DIGIT", true);
        end = new Token("DIGIT", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);
        stack.push(overallEnd);

        State smallLetterDigit = new State(stack);
        smallLetterDigit.setFinal(true);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransition(smallLetter, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransition(smallLetter, letter, smallLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransition(smallLetter, letter, smallLetterDigit);
        }
        overallEnd = new Token("CONSTANT", false);
        start = new Token("DIGIT", true);
        end = new Token("DIGIT", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);
        stack.push(overallEnd);

        State upperLetterDigit = new State(stack);
        upperLetterDigit.setFinal(true);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransition(upperLetter, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransition(upperLetter, letter, upperLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransition(upperLetter, letter, upperLetterDigit);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransition(smallLetterDigit, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransition(smallLetterDigit, letter, smallLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransition(smallLetterDigit, letter, smallLetterDigit);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransition(upperLetterDigit, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransition(upperLetterDigit, letter, upperLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransition(upperLetterDigit, letter, upperLetterDigit);
        }
        return nfa;
    }

    /**
     * A temporary NFA used for testing.
     */
    private static NFA testNFA2() {
        State startState = new State();
        MapBasedNFA nfa = new MapBasedNFA(startState);

        Token start = new Token("NUM", true);
        Token end = new Token("NUM", false);
        Token overallStart = new Token("ZERO_STRING", true);
        Token overallStart2 = new Token("ONE_STRING", true);

        Stack<Token> stack = new Stack<Token>();
        stack.push(overallStart);
        stack.push(overallStart2);
        stack.push(start);
        stack.push(end);
        State actStart = new State(stack);
        nfa.addTransition(startState, null, actStart);

        nfa.addTransition(actStart, '0', actStart);
        nfa.addTransition(actStart, '1', actStart);

        start = new Token("ZERO", true);
        end = start.opposite();
        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);
        State zero = new State(stack);
        nfa.addTransition(actStart, '0', zero);

        start = new Token("ONE", true);
        end = start.opposite();
        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);
        State one = new State(stack);
        nfa.addTransition(actStart, '1', one);

        Token overallEnd = overallStart2.opposite();
        start = new Token("ONE", true);
        end = start.opposite();
        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);
        stack.push(overallEnd);
        State zeroOne = new State(stack);
        zeroOne.setFinal(true);
        nfa.addTransition(zero, '1', zeroOne);

        overallEnd = overallStart.opposite();
        start = new Token("ZERO", true);
        end = start.opposite();
        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);
        stack.push(overallEnd);
        State oneZero = new State(stack);
        oneZero.setFinal(true);
        nfa.addTransition(one, '0', oneZero);

        return nfa;
    }

    /**
     * A temporary NFA used for testing.
     */
    private static NFA testNFA3() {


        State[] states = new State[10];
        for (int i = 0; i < 10; i++) {
            states[i] = new State();
        }
        states[7].setFinal(true);
        MapBasedNFA nfa = new MapBasedNFA(states[9]);


        nfa.addTransition(states[9], null, states[8]);
        nfa.addTransition(states[9], null, states[4]);

        nfa.addTransition(states[8], null, states[0]);
        nfa.addTransition(states[8], null, states[2]);

        nfa.addTransition(states[0], 'a', states[1]);

        nfa.addTransition(states[2], 'b', states[3]);

        nfa.addTransition(states[1], null, states[8]);
        nfa.addTransition(states[1], null, states[4]);

        nfa.addTransition(states[3], null, states[8]);
        nfa.addTransition(states[3], null, states[4]);

        nfa.addTransition(states[4], 'a', states[5]);

        nfa.addTransition(states[5], null, states[6]);

        nfa.addTransition(states[6], 'b', states[7]);

        nfa.addTransition(states[7], null, states[4]);
        return nfa;
    }
}
