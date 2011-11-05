/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generateNFA;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import automata.NFA;
import automata.MapBasedNFA;
import automata.Token;
import automata.State;

import tools.*;

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
            miniNFAs.put(a.getKey(),parser.regex().getCurrentNFA());
        }
        
        return null;
    }
    
}
