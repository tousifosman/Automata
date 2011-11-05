/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.Map;
import automata.CharToken;
import automata.Token;
import exceptions.SyntaxErrorException;
import generateNFA.FinalNFA;

import java.util.LinkedList;


/**
 *
 * @author Paul
 */
public class ScannerTest {

    private static SpecFileScanner scanner;

    public static void main(String[] args) throws java.io.FileNotFoundException, java.io.IOException, SyntaxErrorException {
        System.out.println("Scanner instantiated.");
        scanner = new SpecFileScanner("sample_input_specification.txt");
        System.out.println("Done scanning.");

        System.out.println("charClassDefs--------------------------------");
        for (Map.Entry<String, String> a : scanner.charClassDefs().entrySet()) {
            System.out.println(a.getKey() + ": " + a.getValue());
        }
        System.out.println("____________________________________________|");

        System.out.println("identifiers----------------------------------");
        for (Map.Entry<String, String> a : scanner.identifiers().entrySet()) {
            System.out.println(a.getKey() + ": " + a.getValue());
        }
        System.out.println("____________________________________________|");

        System.out.println("charClasses----------------------------------");
        for (Map.Entry<String, CharToken> a : scanner.charClasses().entrySet()) {
            StringBuilder charlist = new StringBuilder();
            for (Character b : a.getValue().chars()) {
                charlist.append(b);
            }
            System.out.println(a.getKey() + " / " + a.getValue().name() + " := " + charlist.toString());
        }
        System.out.println("____________________________________________|");

        System.out.println("identifierDefs-------------------------------");
        StringBuilder value;
        for (Map.Entry<String, LinkedList<Token>> a : scanner.identifierDefs().entrySet()) {
            value = new StringBuilder();
            for (Token b : a.getValue()) {
                value.append(b.getValue());
            }
            System.out.println(a.getKey() + ": " + a.getValue());
        }
        System.out.println("____________________________________________|");
        
        
        FinalNFA finalNFA = new FinalNFA();
        finalNFA.generate("sample_input_specification.txt");
        
        
    }
}
