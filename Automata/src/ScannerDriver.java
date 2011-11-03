
import automata.DFA;
import automata.MapBasedDFA;
import automata.State;
import automata.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;


public class ScannerDriver {
    private DFA dfa;
    private final File file;
    private XMLBuilder builder;

    public ScannerDriver(String fileName, DFA dfa) throws FileNotFoundException {
        this.file = new File(fileName);
        if (!this.file.exists()) throw new FileNotFoundException();
        this.dfa = dfa;
        builder = new XMLBuilder();
    }

    public static void main(String[] args) {
        // TODO : Replace with generating DFA
        DFA dfa = defaultDFA();

        try {
            ScannerDriver driver = new ScannerDriver("sample_code.txt", dfa);
            driver.run();
        } catch (FileNotFoundException ex) {
            System.out.println("Error: Cannot find sample_code.txt");
        }
    }

    public void run() {
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                String word = scan.next();
                this.parse(word);
            }
        } catch (FileNotFoundException ex) {
        }
        
        System.out.println();
        System.out.println(builder.toString());
    }

    public void parse(String word) {
        builder.reset();

        State currState = dfa.startState();

        for (char character : word.toCharArray()) {
            currState = dfa.transition(currState, character);
            builder.xmlize(character, currState.getTokens());
        }
        builder.finalizeXML();

        if (currState.isFinal()) System.out.println(word + ": ACCEPT");
        else System.out.println(word + ": REJECT");
    }

    private static DFA defaultDFA() {
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
}
