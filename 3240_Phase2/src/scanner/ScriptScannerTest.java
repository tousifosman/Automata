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
        for (String a : ScriptScanner.tokens) {
            System.out.println(a);
        }
    }
    
}
