package tools;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import automata.Token;
import automata.CharToken;
import automata.NFA;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Deque;
import java.util.LinkedList;

/**
 *
 * @author Paul
 */
public class Scanner {

    public static Map<String, CharToken> charClasses;
    public static Map<String, NFA> identifiers;
    //private Set<Token> tokenList = new HashSet<Token>();
    private String currentLine, buffer;
    private FileReader file;
    private BufferedReader reader;
    private Token currentToken;
    private Deque<Character> tokenBuffer = new LinkedList<Character>();
    private Deque<Character> lookAhead = new LinkedList<Character>();
    public boolean busy;

    public Scanner(String filename) throws FileNotFoundException, IOException {
        this.busy = true;
        /* Open file */
        file = new FileReader(filename);
        reader = new BufferedReader(file);

        scanCharClasses();

        this.busy = false;
    }

    private void scanLine() throws IOException {
        /* Keep scanning while lines remain */
        if ((currentLine = reader.readLine()) == null) {
            // End of File
        }
    }

    private void scanCharClasses() throws IOException {
        /* Ignore line comments in beginning of file */
        while (currentLine.startsWith("%")) {
            scanLine();
        }

        charClasses = new HashMap<String, CharToken>();

        while ((currentLine = reader.readLine()).startsWith("$")) {
            char[] chars = currentLine.toCharArray();
            StringBuilder CNBuilder = new StringBuilder();
            int i = 0;

            while (chars[i] == ' ' || chars[i] == '\t' && i++ < chars.length) {
                CNBuilder.append(chars[i]);
            }

            String charClassName = CNBuilder.toString();

            while (chars[i++] != '[') {
                if (i > chars.length) {
                    syntaxError("Missing \'[\' in character class \'" + charClassName + "\'!");
                }
            }

            boolean opposite = false;

            Set<Character> charSet = new HashSet<Character>();
            for (i = i + 1; i < chars.length; i++) {

                if (chars[i] == ']') {

                    break;
                }

                if (chars[i] == '^') {
                    opposite = true;
                }
                if (chars[i] == '\\') {
                    //i++;
                    int newIndex = i + 1;
                    if (Constants.specialCharsList.contains(chars[newIndex])) {
                        charSet.add(chars[newIndex]);
                        i++;
                    }
                }
                if (Constants.upperCaseList.contains(chars[i])) {
                    char startChar = chars[i];
                    charSet.add(startChar);
                    if (chars[i + 1] == '-') {
                        i++;
                        int startIndex = Constants.upperCaseList.indexOf(startChar);
                        i++;
                        int endIndex = Constants.upperCaseList.indexOf(chars[i]) + 1;
                        for (int index = startIndex; index < endIndex; index++) {
                            charSet.add(Constants.upperCaseList.get(index));
                        }

                    }
                } else if (Constants.lowerCaseList.contains(chars[i])) {
                    char startChar = chars[i];
                    charSet.add(startChar);
                    if (chars[i + 1] == '-') {
                        i++;
                        int startIndex = Constants.lowerCaseList.indexOf(startChar);
                        i++;
                        int endIndex = Constants.lowerCaseList.indexOf(chars[i]) + 1;
                        for (int index = startIndex; index < endIndex; index++) {
                            charSet.add(Constants.lowerCaseList.get(index));
                        }
                    }
                } else if (Constants.digitsList.contains(chars[i])) {
                    char startChar = chars[i];
                    charSet.add(startChar);
                    if (chars[i + 1] == '-') {
                        i++;
                        int startIndex = Constants.digitsList.indexOf(startChar);
                        i++;
                        int endIndex = Constants.digitsList.indexOf(chars[i]) + 1;
                        for (int index = startIndex; index < endIndex; index++) {
                            charSet.add(Constants.digitsList.get(index));
                        }
                    }

                }
            }
            if (opposite) {
                while (chars[i] != '$') {
                    i++;
                }
                StringBuilder builder = new StringBuilder();
                for (i = 1; i < chars.length; i++) {
                    if (chars[i] == ' ' || chars[i] == '\t') {
                        break;
                    }
                    builder.append(chars[i]);
                }
                String className = builder.toString().trim();
                Set<Character> newCharSet = new HashSet<Character>();
                for (Character c : charClasses.get(className).chars) {
                    newCharSet.add(c);
                }
                for (Character c : charSet) {
                    newCharSet.remove(c);
                }
                charSet = newCharSet;
            }
            CharToken token = new CharToken(charClassName, charSet);
            charClasses.put(charClassName, token);
        }
        if (!currentLine.startsWith("%")) {
            syntaxError("Can't find Token definitions!");
        }
    }

    private void syntaxError(String message) {
        System.out.println(message);
        Runtime.getRuntime().halt(1);
    }

    private void scanToken() throws IOException {
        if (lookAhead.isEmpty()) {
            // Starting a new line
            scanLine();
            for (int i = 0; i < currentLine.length(); i++) {
                lookAhead.addLast(currentLine.toCharArray()[i]);
            }
            /* We are reading an identifier */
            newIdentifier();
        } else {
            /* Starting at some random point */
            if (pop() == null) {
                if (tokenBuffer.isEmpty()) {
                    currentToken = Constants.EPSILON;
                    return;
                } else {
                    syntaxError("Unexpected end of line!");
                }
            }
            switch (tokenBuffer.peekFirst()) {
                case '|':
                    currentToken = Constants.UNION;
                    break;
                case '(':
                    currentToken = Constants.LPAREN;
                    break;
                case ')':
                    currentToken = Constants.RPAREN;
                    break;
                case '*':
                    currentToken = Constants.ASTERISK;
                    break;
                case '+':
                    currentToken = Constants.PLUS;
                    break;
                case '.':
                    currentToken = Constants.FULLSTOP;
                    break;
                case '^':
                    currentToken = Constants.CARAT;
                    break;
                case '[':
                    currentToken = Constants.LBRACKET;
                    break;
                case ']':
                    currentToken = Constants.RBRACKET;
                    break;
                case '?':
                    currentToken = Constants.QUESTION;
                    break;
            }
            /*HANDLE BACKSLASH and DOLLAR SIGN*/
            
            //if (!tokenList.contains(currentToken)) tokenList.add(currentToken);
        }
    }

    private void newIdentifier() {
        if (pop() != '$') {
            syntaxError("Missing \'$\' leading Identifier!");
        }
        while (lookAhead.pollFirst() != ' ' && pop() != '\t') {
        }
        /* Space marks the end of an identifier */
        while (!tokenBuffer.isEmpty()) {
            buffer.concat(tokenBuffer.pop().toString());
        }
        currentToken = new Token(buffer.trim(), false);
        //if (!tokenList.contains(currentToken)) tokenList.add(currentToken);
    }

    private Character pop() {
        tokenBuffer.push(lookAhead.pollFirst());
        return tokenBuffer.peekFirst();
    }

    private void unpop() {
        lookAhead.push(tokenBuffer.pollFirst());
    }

    public Token peekToken() throws IOException {
        scanToken();
        return currentToken;
    }

    public void matchToken(Token token) {
        return;
    }
}
