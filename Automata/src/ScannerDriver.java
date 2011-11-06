
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
            while (scan.hasNext()) {
                // Uses space delimeter by default, may want to change that
                // in the future.
                String word = scan.next();
                this.parse(word);
            }
            
            builder.exportXML(file.getName().substring(0, file.getName().lastIndexOf('.')));
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            System.out.println("Unable to write out xml files.");
        }
    }

    public void parse(String word) {
        builder.reset();

        State currState = dfa.startState();

        for (char character : word.toCharArray()) {
            currState = dfa.transition(currState, character);
            if(currState != null){
            	builder.xmlize(character, currState.getTokens());
            }
        }
        builder.finalizeXML();
        if(currState != null){
	        if (currState.isFinal()) {
	            if (!currState.getTokens().isEmpty())
	                System.out.println(word + ": ACCEPT (" + currState.topToken().getValue() + ")");
	            else
	                System.out.println(word + ": ACCEPT");
	        } else System.out.println(word + ": REJECT");
        }
        else {
        	System.out.println(word + ": REJECT");
        }
    }
}
