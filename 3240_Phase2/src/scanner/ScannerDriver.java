package scanner;

import scanner.automata.DFA;
import scanner.automata.State;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Takes in a DFA then verifies that a given file contains valid tokens. 
 * 
 * It can also generate an XML-structure of the tokens, showing how the characters
 * form into compounding tokens.
 */
public class ScannerDriver {

    private DFA dfa;
    private final File file;

    /**
     * Creates and initializes the ScannerDriver
     * @param fileName The name of the file containing all of the tokens
     * @param dfa The DFA to use while scanning
     * @throws FileNotFoundException thrown if the file given doesn't exist.
     */
    public ScannerDriver(String fileName, DFA dfa) throws FileNotFoundException {
        this(new File(fileName), dfa);
    }

    /**
     * Creates and initializes the ScannerDriver
     * @param inputFile The File containing all of the tokens
     * @param dfa The DFA to use while scanning
     * @throws FileNotFoundException thrown if the file given doesn't exist.
     */
    public ScannerDriver(File inputFile, DFA dfa) throws FileNotFoundException {
        this.file = inputFile;
        if (!this.file.exists()) {
            throw new FileNotFoundException();
        }
        this.dfa = dfa;
    }

    /**
     * Initiates the scan process.
     */
    public void run() {
        try {
            int lineNum = 0;
            Scanner scan = new Scanner(file);
            String longestToken;
            while (scan.hasNextLine()) {
                longestToken = "";
                String line = scan.nextLine();
                lineNum++;
                int startIndex = 0;
                int endIndex = 0;
                while (endIndex < line.length()) {
                    longestToken = "";
                    boolean accepted = false;

                    // Correct for scanner problems on single character tokens, >!, and regexes.
                    switch (line.charAt(startIndex)) {
                        case '=':
                        case '#':
                        case ';':
                        case ')':
                        case '(':
                        case ',':
                            newToken(line.substring(startIndex, startIndex + 1));
                            startIndex++;
                            endIndex = startIndex;
                          //  System.out.println("Single Token: " + line.substring(startIndex - 1, startIndex));
                            continue;
                        case ' ':
                            startIndex++;
                            endIndex = startIndex;
                        //    System.out.println("Skipped space character");
                            continue;
                        case '>':
                            endIndex++;
                            if (line.charAt(endIndex) == '!') {
                                longestToken = line.substring(startIndex, endIndex + 1);
                            }
                            break;
                        case '\'':
                            endIndex++;
                            while (endIndex < line.length()) {
                                if (line.charAt(endIndex) == '\'' && line.charAt(endIndex - 1) != '\\') {
                                    longestToken = line.substring(startIndex, endIndex + 1);
                                    break;
                                }
                                endIndex++;
                            }
                            break;
                    }
//                    while (!accepted && endIndex < line.length()) {
//                        endIndex += 1;
//                        accepted = parse(line.substring(startIndex, endIndex));
//                    }
//
//                    while (accepted && endIndex < line.length()) {
//                        endIndex += 1;
//                        accepted = parse(line.substring(startIndex, endIndex));
//                    }
                    if (longestToken.length() <= 0) {
                        while (endIndex < line.length()) {
                            accepted = parse(line.substring(startIndex, endIndex + 1));
                            if (accepted) {
                                longestToken = line.substring(startIndex, endIndex + 1);
                            }
                            endIndex++;
                        }
                    }

                    if (longestToken.length() <= 0) {
                        startIndex++;
                        endIndex = startIndex;
                       // System.out.println("No token, advancing pointer.");
                        continue;
                    }

                    startIndex += longestToken.length();
                    endIndex = startIndex;
                   // System.out.println("Longest Token: " + longestToken);
                    newToken(longestToken);
                }
            }
            // builder.exportXML(file.getName().substring(0, file.getName().lastIndexOf('.')));
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            System.out.println("Unable to write output.");
        }
    }

    public boolean parse(String word) {
        // builder.reset();

        State currState = dfa.startState();

        for (char character : word.toCharArray()) {
            currState = dfa.transition(currState, character);
//            if (currState != null) {
//                builder.xmlize(character, currState.getTokens());
//            }
        }
//        builder.finalizeXML();
        if (currState != null) {
            if (currState.isFinal()) {
                if (currState.getTokens().isEmpty()) {
                 //   System.out.println(word + ": ACCEPT (" + currState.topToken().getValue() + ")");
                }

              //  System.out.println(word + ": ACCEPT");
                return true;
            } else {
              //  System.out.println(word + ": REJECT");
                return false;
            }

        } else {
          //  System.out.println(word + ": REJECT");
            return false;
        }
        // return false;
    }

    public void error(int line) {
        System.out.println("Error at line " + line);
    }
    private String[] keywords = {"begin", "end", "=", "replace", "with", "in", ";", "recursivereplace", ">!", "print", "(", ")", ",", "#", "find", "diff", "union", "inters", "maxfreqstring"};
    private List<String> keywordList = Arrays.asList(keywords);

    public void newToken(String token) {
        if (token.length() < 1) {
            error(0);
        }
        ScriptScanner.tokens.add(token);
        switch (token.charAt(0)) {
            case '\'':
                ScriptScanner.regexes.add(token);
                break;
            case '\"':
                ScriptScanner.strconsts.add(token);
                break;
            default:
                if (!keywordList.contains(token)) {
                    ScriptScanner.identifiers.add(token);
                }
        }
    }
}
