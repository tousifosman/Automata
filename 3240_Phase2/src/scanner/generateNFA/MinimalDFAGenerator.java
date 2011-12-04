package scanner.generateNFA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import scanner.automata.CharToken;
import scanner.automata.NFA;
import scanner.tools.Constants;

/**
 * A class to perform an algorithmic minimization of a table-driven DFA.
 * @author
 */
public class MinimalDFAGenerator {

    private Map<String, CharToken> charClasses;
    private Map<String, NFA> identifiers;

    public MinimalDFAGenerator(String fileName) {


        try {
            FileReader file = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(file);
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.startsWith("%%") && line.contains("character")) {
                    charClasses = new HashMap<String, CharToken>();
                    while ((line = reader.readLine()) != null && !line.startsWith("%%")) {
                        //System.out.println(line);

                        char[] chars = line.toCharArray();
                        StringBuilder CNBuilder = new StringBuilder();
                        int i;
                        for (i = 0; i < chars.length; i++) {
                            if (chars[i] != ' ' && chars[i] != '\t') {
                                CNBuilder.append(chars[i]);
                            } else {
                                break;
                            }
                        }
                        String charClassName = CNBuilder.toString();

                        while (chars[i] != '[') {
                            i++;
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
                            for (i = i; i < chars.length; i++) {
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
                    for (String key : charClasses.keySet()) {
                        CharToken token = charClasses.get(key);
                        System.out.print(key + ": ");
                        for (Character c : token.chars) {
                            System.out.print(c + ",");
                        }
                        System.out.println();

                    }
                    System.out.println("\n\n\n");


                    if (line.startsWith("%%") && line.contains("Token")) {
                        identifiers = new HashMap<String, NFA>();
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);

                            constuctNFA(line);
                        }
                    }

                }

            }



        } catch (Exception ex) {
            //System.out.println("Error: Cannot find file");
            //System.out.println(ex.getStackTrace());
            ex.printStackTrace();
        }

    }

    private void constuctNFA(String regex) {
        if (regex.length() < 1) {
            return;
        }
        //JW TODO construct DFAs here
        Map<String, Stack<String>> idMap = getTokens(regex);
        Stack<String> idStack = idMap.values().iterator().next();
        String idName = idMap.keySet().iterator().next();

        System.out.println("ID: " + idName);

        while (!idStack.isEmpty()) {
            System.out.println(idStack.pop());
        }
    }

    private Map<String, Stack<String>> getTokens(String regex) {

        Stack<String> idStack = new Stack<String>();

        char[] chars = regex.toCharArray();
        int i = 0;
        StringBuilder idNameBuilder = new StringBuilder();
        for (i = i; i < chars.length; i++) {
            if (chars[i] == ' ' || chars[i] == '\t') {
                break;
            }
            idNameBuilder.append(chars[i]);
        }
        String idName = idNameBuilder.toString();
        //idStack.push(idName);

        while (chars[i] == ' ' || chars[i] == '\t') {
            i++;
        }
        StringBuilder nameBuilder = new StringBuilder();
        for (i = i; i < chars.length; i++) {

            if (Constants.operatorList.contains(chars[i])) {
                if (nameBuilder.toString().length() > 1) {
                    idStack.push(nameBuilder.toString());
                }
                nameBuilder = new StringBuilder();
                idStack.push(Character.toString(chars[i]));
                continue;
            } else if (chars[i] != ' ' && chars[i] != '\t') {
                nameBuilder.append(chars[i]);
            } else {
                if (nameBuilder.toString().length() > 1) {
                    idStack.push(nameBuilder.toString());
                    nameBuilder = new StringBuilder();
                }
            }

        }
        Map<String, Stack<String>> idMap = new HashMap<String, Stack<String>>();

        idMap.put(idName, idStack);

        return idMap;
    }

    /**
     * Returns related character classes.
     * @return Map of character classes.
     */
    public Map<String, CharToken> getCharClasses() {
        return charClasses;
    }

    /**
     * Returns related tokens.
     * @return Map of tokens.
     */
    public Map<String, NFA> getIdentifiers() {
        return identifiers;
    }
}
