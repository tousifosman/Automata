/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import scanner.exceptions.SyntaxErrorException;


/**
 *
 * @author Paul
 */
public class ScriptScannerTest {
    public static void main(String[] args) throws java.io.FileNotFoundException, java.io.IOException, SyntaxErrorException {
        ScriptScanner.scan();
        System.out.println("-> Tokens:");
        for (String a : ScriptScanner.tokens) {
            System.out.println(a);
        }
        System.out.println("-> String Constants:");
        for (String a : ScriptScanner.strconsts) {
            System.out.println(a);
        }
        System.out.println("-> Identifiers:");
        for (String a : ScriptScanner.identifiers) {
            System.out.println(a);
        }
        System.out.println("-> Regexes:");
        for (String a : ScriptScanner.regexes) {
            System.out.println(a);
        }
        System.out.println("End");
    }
    
}
