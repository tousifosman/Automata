/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generateNFA;

import java.util.Map;
import java.util.HashMap;
import automata.NFA;
import automata.MapBasedNFA;
import automata.Token;
import automata.State;

import tools.*;

/**
 *
 * @author Paul
 */
public class finalNFA {

    private SpecFileScanner scan;
    private Map<String, String> regexes;
    private Map<String, NFA> miniNFAs;

    public finalNFA(String filename) throws java.io.FileNotFoundException, java.io.IOException, exceptions.SyntaxErrorException {
        scan = new SpecFileScanner(filename);
        
    }
}
