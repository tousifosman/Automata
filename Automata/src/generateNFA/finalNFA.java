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

    private Scanner scanner;
    private Map<String, String> regexes;
    private Map<String, NFA> miniNFAs;

    public finalNFA(String filename) {
        try {
            scanner = new Scanner(filename);
        } catch (java.io.FileNotFoundException e) {
        } catch (java.io.IOException ee) {
        }
        while (scanner.isBusy()) {
        }
         scanner.enable();
        miniNFAs = new HashMap<String, NFA>();
        while(!scanner.EOF) {
            scanner.disable();
            regexes.put(scanner.newIdentifier(), scanner.restOfLine());
            scanner.enable();
        }
        scanner.EOF = false;
        for (String a : regexes.keySet()) {
            scanner.currentLine = regexes.get(a);
            miniNFAs.put(a, /*TODO: Call Recurive Descent Here*/ new MapBasedNFA(new State()));
        }
        
    }
}
