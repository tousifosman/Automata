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
import java.util.Set;
import java.util.Deque;
import java.util.LinkedList;
import java.util.TreeSet;
import automata.CharToken;
import automata.Token;

/**
 * A Scanner for performing lexical analysis, and limited parsing, of language specification input files. Validates and parses character classes and tokens into Map data structures. Breaks apart token definitions into regex tokens, to be fed into the RecursiveDescent parser by the RegexScanner class.
 * @author Paul
 */
public class SpecFileScanner {

    private Map<String, String> charClassDefs, identifiers;
    private TreeMap<String, CharToken> charClasses;
    private Map<String, LinkedList<Token>> identifierDefs;
    private Scanner jScanner, jScanner2;

    /**
     * A tree map of all defined character classes.
     * @return A map with entries of the form <Class Name, Unparsed Token>.
     */
    public TreeMap<String, CharToken> charClasses() {
        return charClasses;
    }

    /**
     * A map of defined tokens.
     * @return A map with entries of the form <Token Name, Definition as List of Regex Tokens>.
     */
    public Map<String, LinkedList<Token>> identifierDefs() {
        return identifierDefs;
    }

    /**
     * A map of character class definitions.
     * @return A map with entries containing the raw Strings as read from the input specification file.
     */
    public Map<String, String> charClassDefs() {
        return charClassDefs;
    }

    /**
     * A map of token definitions.
     * @return A map with entries containing the raw Strings as read from the input specification file.
     */
    public Map<String, String> identifiers() {
        return identifiers;
    }

    /**
     * 
     * @param fileName
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SyntaxErrorException 
     */
    public SpecFileScanner(String fileName) throws FileNotFoundException, IOException, SyntaxErrorException {
        jScanner = new Scanner(new FileInputStream(fileName));
        log("Scanner Object successfully instantiated, scan process commencing...");
        scan();
    }

    /**
     * Contains the scanning procedure. Processes both the character class section and token definitions, then calling the respective parser function for each.
     * @throws IOException
     * @throws SyntaxErrorException 
     */
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
                if (curr.length() <= 0) {
                    continue;
                }
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
            StringBuilder regex;
            while (jScanner.hasNextLine()) {
                regex = new StringBuilder();
                line++;
                curr = jScanner.nextLine();
                System.out.println(curr);
                log("Line " + line + ": " + curr);
                if (curr.length() <= 0) {
                    continue;
                }
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

    /**
     * Parser-lexer combination procedure which processes the character class section of the input specification file.
     * @throws SyntaxErrorException 
     */
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
                    for (Character b : charClasses.get(excClass.toString()).chars()) {
                        superSet.add(new Character(b.charValue()));
                    }
                    for (Character b : currClass) {
                        superSet.remove(b);
                    }
                    currClass = superSet;
                } else {
                    throw new SyntaxErrorException();
                }
            }
            charClasses.put(a.getKey(), new CharToken(a.getKey(), currClass));
        }
    }

    /**
     * Parser-lexer combination procedure which processes the token definition section of the input specification file.
     */
    private void scanIdentifiers() {
        Deque<Character> tokenBuffer = new LinkedList<Character>(), curr = new LinkedList<Character>();
        identifierDefs = new TreeMap<String, LinkedList<Token>>();
        for (Map.Entry<String, String> a : identifiers.entrySet()) {
            /* Convert regex String into a Linked List of constituent characters */
        	for (char b : a.getValue().toCharArray()) {
                curr.add(b);
            }
            curr.add(' ');
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
                        if(!temp.toString().trim().equals("\\")){
                        	tokens.add(new Token(temp.toString().trim()));
                        }
                        break;
                    case '$':
                        /* Character class */
                        do {
                            tokenBuffer.push(curr.pop());
                        } while (!Constants.specialCharsList2.contains(tokenBuffer.peek()) && !Character.isWhitespace(tokenBuffer.peek()) && tokenBuffer.peek() != ' ');
                        curr.push(tokenBuffer.pop()); //Reverses the extra lookAhead
                        while (!tokenBuffer.isEmpty()) {
                            temp.append(tokenBuffer.pollLast());
                        }
                        tokens.add(new Token(temp.toString().trim()));
                        break;
                    default:
                        /* Single Character */
                        if (!Character.isWhitespace(tokenBuffer.peek()) && tokenBuffer.peek() != ' ') {
                            while (!tokenBuffer.isEmpty()) {
                                temp.append(tokenBuffer.pollLast());
                            }
                            tokens.add(new Token(temp.toString().trim()));
                        }
                }
            }
            identifierDefs.put(a.getKey(), tokens);
        }
    }

    /**
     * Generic logging function. Suppresses output by default, but can be manually edited to enable verbose console output or logging.
     * @param message Message to be recorded.
     */
    private void log(String message) {
        /* Uncomment the following line for verbose console output. */
        //System.out.println("> [" + System.nanoTime() / 60000000 + "] " + message);
    }
}
