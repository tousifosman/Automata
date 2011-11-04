
import automata.*;
import conversion.NFAtoDFA;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Main {
    /**
     * Creates the DFA and ScannerDriver using the files containing the regexes
     * and the tokens.
     * 
     * If no arguments are supplied, the user is prompted for inputs
     * 
     * @param args 1st argument is used as the name of the file containing all 
     * of the tokens
     */
    public static void main(String[] args) {
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

        DFA dfa;
        if (n == 0) {
            dfa = generateDFA();
        } else {
            dfa = loadDFA();
        }

        JOptionPane.showMessageDialog(null, "Next, choose the test cases to examine.");
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        File inputFile = fc.getSelectedFile();

        try {
            ScannerDriver driver = new ScannerDriver(inputFile, dfa);
            driver.run();
        } catch (FileNotFoundException ex) {
            System.out.println("Error: Cannot find file");
            System.out.println(ex.getMessage());
        }
    }

    private static DFA loadDFA() {
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return DFAExportImport.importDFA(file);
    }

    private static DFA generateDFA() {
        // TODO : Replace with generating NFA
        NFA nfa = testNFA();
        DFA dfa = NFAtoDFA.dfaFromNFA(nfa); //DFA dfa = defaultDFA();
        // TODO : DFA minimization

        saveDFA(dfa);

        return dfa;
    }

    private static void saveDFA(DFA dfa) {
        File exportDFA = new File("dfa.txt");
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
            dfa.addTransisition(startState, letter, small);
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
            dfa.addTransisition(startState, letter, upper);
        }
        start = new Token("DIGIT", true);
        end = new Token("DIGIT", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);

        State fail = new State(stack);
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransisition(startState, letter, fail);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransisition(fail, letter, fail);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransisition(fail, letter, fail);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransisition(fail, letter, fail);
        }
        start = new Token("LETTER", true);
        end = new Token("LETTER", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);

        State smallLetter = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransisition(small, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransisition(small, letter, smallLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransisition(small, letter, fail);
        }

        start = new Token("LETTER", true);
        end = new Token("LETTER", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);

        State upperLetter = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransisition(upper, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransisition(upper, letter, upperLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransisition(upper, letter, fail);
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
            dfa.addTransisition(smallLetter, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransisition(smallLetter, letter, smallLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransisition(smallLetter, letter, smallLetterDigit);
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
            dfa.addTransisition(upperLetter, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransisition(upperLetter, letter, upperLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransisition(upperLetter, letter, upperLetterDigit);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransisition(smallLetterDigit, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransisition(smallLetterDigit, letter, smallLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransisition(smallLetterDigit, letter, smallLetterDigit);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            dfa.addTransisition(upperLetterDigit, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            dfa.addTransisition(upperLetterDigit, letter, upperLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            dfa.addTransisition(upperLetterDigit, letter, upperLetterDigit);
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
            nfa.addTransisition(startState, letter, small);
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
            nfa.addTransisition(startState, letter, small2);
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
            nfa.addTransisition(small2, letter, small2Digit);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransisition(small2Digit, letter, small2Digit);
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
            nfa.addTransisition(startState, letter, upper);
        }
        start = new Token("LETTER", true);
        end = new Token("LETTER", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);

        State smallLetter = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransisition(small, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransisition(small, letter, smallLetter);
        }

        start = new Token("LETTER", true);
        end = new Token("LETTER", false);

        stack = new Stack<Token>();
        stack.push(start);
        stack.push(end);

        State upperLetter = new State(stack);
        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransisition(upper, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransisition(upper, letter, upperLetter);
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
            nfa.addTransisition(smallLetter, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransisition(smallLetter, letter, smallLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransisition(smallLetter, letter, smallLetterDigit);
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
            nfa.addTransisition(upperLetter, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransisition(upperLetter, letter, upperLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransisition(upperLetter, letter, upperLetterDigit);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransisition(smallLetterDigit, letter, smallLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransisition(smallLetterDigit, letter, smallLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransisition(smallLetterDigit, letter, smallLetterDigit);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            nfa.addTransisition(upperLetterDigit, letter, upperLetter);
        }
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            nfa.addTransisition(upperLetterDigit, letter, upperLetter);
        }
        for (char letter = '0'; letter <= '9'; letter++) {
            nfa.addTransisition(upperLetterDigit, letter, upperLetterDigit);
        }
        return nfa;
    }
}
