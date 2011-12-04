package scanner;

import automata.*;
import conversion.NFAtoDFA;
import generateNFA.FinalNFA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import exceptions.SyntaxErrorException;
import java.util.regex.Pattern;

public class ScriptScanner {

    public static List<String> identifiers;
    public static List<String> regexes;
    public static List<String> strconsts;
    public static Deque<String> tokens;

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
    public static void scan() throws FileNotFoundException, IOException, SyntaxErrorException {
        identifiers = new ArrayList<String>();
        regexes = new ArrayList<String>();
        strconsts = new ArrayList<String>();
        tokens = new ArrayDeque<String>();

        System.out.println("Using Lexical Specification for MinRE: " + ScriptScanner.class.getResource("MiniRE_LexSpec.txt").toString().substring(6));
        DFA dfa = generateDFA(ScriptScanner.class.getResource("MiniRE_LexSpec.txt").toString().substring(6));


        File inputFile;

        JOptionPane.showMessageDialog(null, "Please choose an input script.");
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        inputFile = fc.getSelectedFile();

        try {
            ScannerDriver driver = new ScannerDriver(inputFile, dfa);
            driver.run();
        } catch (FileNotFoundException ex) {
            System.out.println("Error: Cannot find file");
            System.out.println(ex.getMessage());
        }
    }

    private static DFA generateDFA(String fileName) throws FileNotFoundException, IOException, SyntaxErrorException {
        FinalNFA NFAgen = new FinalNFA();
        NFA nfa  = NFAgen.generate(fileName);
        return NFAtoDFA.dfaFromNFA(nfa);
    }
}
