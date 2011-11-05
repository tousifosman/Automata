/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.*;
import java.util.Scanner;
import exceptions.SyntaxErrorException;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.Deque;
import java.util.LinkedList;
import java.util.TreeSet;
import automata.CharToken;
import automata.Token;

/**
 *
 * @author Paul
 */
public class SpecFileScanner {

    private Map<String, String> charClassDefs, identifiers;
    private TreeMap<String, CharToken> charClasses;
    private Map<String, LinkedList<Token>> identifierDefs;
    private Scanner jScanner, jScanner2;

    public TreeMap<String, CharToken> charClasses() {
        return charClasses;
    }

    public Map<String, LinkedList<Token>> identifierDefs() {
        return identifierDefs;
    }

    public Map<String, String> charClassDefs() {
        return charClassDefs;
    }

    public Map<String, String> identifiers() {
        return identifiers;
    }

    public SpecFileScanner(String fileName) throws FileNotFoundException, IOException, SyntaxErrorException {
        jScanner = new Scanner(new FileInputStream(fileName));
        log("Scanner Object successfully instantiated, scan process commencing...");
        scan();
    }

    private void scan() throws IOException, SyntaxErrorException {
        StringBuilder text = new StringBuilder();
        String newline = System.getProperty("line.separator");
        String curr;
        int line = 0;
        try {
            if (jScanner.hasNextLine()) {
                line++;
                curr = jScanner.nextLine();
                log("Line " + line + ": " + curr);
                if (!curr.startsWith("%")) {
                    throw new SyntaxErrorException();
                }
            }
            charClassDefs = new TreeMap<String, String>();
            String name;
            StringBuilder def = new StringBuilder();
            while (jScanner.hasNextLine()) {
                line++;
                curr = jScanner.nextLine();
                System.out.println(curr);
                log("Line " + line + ": " + curr);
                if (curr.charAt(0) != '$' || curr.charAt(0) == '%') {
                    break;
                }
                jScanner2 = new Scanner(curr);
                name = jScanner2.next();
                while (jScanner2.hasNext()) {
                    def.append(jScanner2.next());
                }
                charClassDefs.put(name, def.toString());
                def = new StringBuilder();
            }
            identifiers = new TreeMap<String, String>();
            StringBuilder regex = new StringBuilder();
            while (jScanner.hasNextLine()) {
                line++;
                curr = jScanner.nextLine();
                System.out.println(curr);
                log("Line " + line + ": " + curr);
                if (curr.charAt(0) != '$') {
                    throw new SyntaxErrorException("Improper formatting in identifier region.");
                }
                jScanner2 = new Scanner(curr);
                name = jScanner2.next();
                while (jScanner2.hasNext()) {
                    regex.append(jScanner2.next());
                }
                identifiers.put(name, regex.toString());
            }
            /* At this point, all of the Character class definitions and the 
             * identifiers are <String, String> entries in Hashmaps
             */
            scanCharClasses();
            scanIdentifiers();
        } finally {
            jScanner.close();
        }
    }

    private void scanCharClasses() throws SyntaxErrorException {
        charClasses = new TreeMap<String, CharToken>();
        for (Map.Entry<String, String> a : charClassDefs.entrySet()) {
            char chars[] = a.getValue().toCharArray(), tmp, next;
            Set<Character> currClass = new TreeSet<Character>();
            int i = 0;
            if (chars[i] != '[' || chars.length < 3) {
                throw new SyntaxErrorException();
            }
            boolean exclusion = true;
            if (i + 1 < chars.length && (tmp = chars[++i]) != '^') {
                exclusion = false;
                if (tmp == '[' || tmp == ']') {
                    throw new SyntaxErrorException();
                }
            }
            while (i + 1 < chars.length && chars[i] != ']') {
                // If ecape character, just replace by next character and continue as if single.
                tmp = chars[i] == '\\' ? chars[++i] : chars[i];
                if (chars[i + 1] == '-') {
                    // Range
                    i += 2;
                    char max = chars[i] == '\\' ? chars[++i] : chars[i];
                    if (tmp > max) {
                        throw new SyntaxErrorException();
                    }
                    for (char j = tmp; j <= max; j++) {
                        currClass.add(new Character(j));
                    }
                } else {
                    currClass.add(new Character(tmp));
                }
                i++;
            }
            if (exclusion) {
                if (i + 1 >= chars.length || chars[++i] != 'I') {
                    throw new SyntaxErrorException();
                }
                if (i + 1 >= chars.length || chars[++i] != 'N') {
                    throw new SyntaxErrorException();
                }
                StringBuilder excClass = new StringBuilder();
                for (i += 1; i < chars.length; i++) {
                    excClass.append(chars[i]);
                }
                if (charClasses.keySet().contains(excClass.toString())) {
                    Set<Character> superSet = new TreeSet<Character>();
                    for (Character b : charClasses.get(excClass.toString()).chars()){
                        superSet.add(new Character(b.charValue()));
                    }
                    for (Character b : currClass) {
                        superSet.remove(b);
                    }
                    currClass = superSet;
                } else throw new SyntaxErrorException();
            }
            charClasses.put(a.getKey(), new CharToken(a.getKey(),currClass));
        }
    }

    

    private void scanIdentifiers() {
        Deque<Character> tokenBuffer = new LinkedList<Character>(), curr = new LinkedList<Character>();
        identifierDefs = new TreeMap<String, LinkedList<Token>>();
        for (Map.Entry<String, String> a : identifiers.entrySet()) {
            /* Convert regex String into a Linked List of constituent characters */
            for (char b : a.getValue().toCharArray()) {
                curr.add(b);
            }
            StringBuilder temp;
            LinkedList<Token> tokens = new LinkedList<Token>();
            while (!curr.isEmpty()) {
                tokenBuffer.push(curr.pop());
                temp = new StringBuilder();
                switch (tokenBuffer.peek()) {
                    case '\\':
                        /* Escaped character token */
                        tokenBuffer.push(curr.pop());
                        while (!tokenBuffer.isEmpty()) {
                            temp.append(tokenBuffer.pollLast());
                        }
                        tokens.add(new Token(temp.toString()));
                        break;
                    case '$':
                        /* Character class */
                        do {
                            tokenBuffer.push(curr.pop());
                        } while (!Constants.specialCharsList.contains(tokenBuffer.peek()) && !Character.isWhitespace(tokenBuffer.peek()) && tokenBuffer.peek() != ' ');
                        curr.push(tokenBuffer.pop()); //Reverses the extra lookAhead
                        while (!tokenBuffer.isEmpty()) {
                            temp.append(tokenBuffer.pollLast());
                        }
                        tokens.add(new Token(temp.toString()));
                        break;
                    default:
                        /* Single Character */
                        if (!Character.isWhitespace(tokenBuffer.peek()) && tokenBuffer.peek() != ' ') {
                            while (!tokenBuffer.isEmpty()) {
                                temp.append(tokenBuffer.pollLast());
                            }
                            tokens.add(new Token(temp.toString()));
                        }
                }
            }
            identifierDefs.put(a.getKey(), tokens);
        }
    }

    private void log(String message) {
        /* Uncomment the following line for verbose console output. */
        //System.out.println("> [" + System.nanoTime() / 60000000 + "] " + message);
    }
}
