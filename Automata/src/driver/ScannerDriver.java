package driver;

import automata.DFA;
import automata.State;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

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

    /**
     * Creates and initializes the ScannerDriver
     * @param inputFile The File containing all of the tokens
     * @param dfa The DFA to use while scanning
     * @throws FileNotFoundException thrown if the file given doesn't exist.
     */
    public ScannerDriver(File inputFile, DFA dfa) throws FileNotFoundException {
        this.file = inputFile;
        if (!this.file.exists()) throw new FileNotFoundException();
        this.dfa = dfa;
        builder = new XMLBuilder();
    }

    /**
     * Initiates the scan process.
     */
    public void run() {
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                int startIndex = 0;
                int endIndex = 0;
                while (endIndex < line.length()) {
                    boolean accepted = false;
                    while (!accepted) {
                        endIndex += 1;
                        accepted = parse(line.substring(startIndex, endIndex));
                    }

                    while (accepted) {
                        endIndex += 1;
                        accepted = parse(line.substring(startIndex, endIndex));
                    }

                    endIndex -= 1;
                    String longestToken = line.substring(startIndex, endIndex);
                    System.out.println("longest token: " + longestToken);
                    startIndex = endIndex;
                }

            }
            builder.exportXML(file.getName().substring(0, file.getName().lastIndexOf('.')));
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            System.out.println("Unable to write out xml files.");
        }
    }

    public boolean parse(String word) {
        builder.reset();

        State currState = dfa.startState();

        for (char character : word.toCharArray()) {
            currState = dfa.transition(currState, character);
            if (currState != null) {
                builder.xmlize(character, currState.getTokens());
            }
        }
        builder.finalizeXML();
        if (currState != null) {
            if (currState.isFinal()) {
                if (!currState.getTokens().isEmpty())
                    System.out.println(word + ": ACCEPT (" + currState.topToken().getValue() + ")");
                else
                    System.out.println(word + ": ACCEPT");
                return true;
            } else {
                System.out.println(word + ": REJECT");
                return false;
            }

        } else {
            System.out.println(word + ": REJECT");
            return false;
        }
    }
}
