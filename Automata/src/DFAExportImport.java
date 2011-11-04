
import automata.DFA;
import automata.MapBasedDFA;
import automata.State;
import automata.Token;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

// TODO : Document me.
public class DFAExportImport {
    public static void exportDFA(DFA dfa, File file) {
        try {
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            writeDFA(dfa, out);
            out.close();
            fstream.close();
        } catch (IOException ex) {
            System.out.println("Error writing DFA to: " + file.toString());
        }
    }

    private static void writeDFA(DFA dfa, Writer out) throws IOException {
        Character[] alphabet = dfa.alphabet().toArray(new Character[0]);
        State[] states = dfa.allStates().toArray(new State[0]);


        out.write("Start: ");
        out.write(dfa.startState().toString());
        out.write("\n");
        out.write("Final: ");
        for (State state : dfa.finalStates()) {
            out.write("\t");
            out.write(state.toString());
        }
        out.write("\n");
        out.write("-");
        for (Character character : alphabet) {
            out.write("\t");
            out.write(character);
        }
        out.write("\n");

        for (State currState : states) {
            out.write(currState.toString());
            //out.write("\t");
            for (Character charater : alphabet) {
                out.write("\t");
                out.write(dfa.transition(currState, charater).toString());
            }
            out.write("\n");
        }
        out.write("--Tokens\n");
        for (State currState : states) {
            Stack<Token> tokens = currState.getTokens();
            out.write(currState.toString());
            while (!tokens.isEmpty()) {
                out.write("\t");
                out.write(tokens.pop().toString());
            }
            out.write("\n");
        }

        out.flush();
    }

    public static DFA importDFA(File file) {
        try {
            Scanner scan = new Scanner(file);
            HashMap<String, State> stringToStateMap = new HashMap<String, State>();
            HashMap<String, String[]> statesTransitions = new HashMap<String, String[]>();
            List<String> statesToBeParsed = new LinkedList<String>();

            State.resetCount();
            String startStateString = scan.nextLine();
            if (!startStateString.startsWith("Start:")) {
                System.out.println("Error: Invalid DFA format");
                System.exit(1);
            }
            startStateString = startStateString.split(" ")[1];

            String finalStatesUnparsed = scan.nextLine();
            int tabIndex = finalStatesUnparsed.indexOf(" ");
            String[] finalStates = finalStatesUnparsed.substring(tabIndex + 1).split("\t");
            HashSet<String> finalStateSet = new HashSet<String>();
            finalStateSet.addAll(Arrays.asList(finalStates));

            String alphabetUnparsed = scan.nextLine();
            tabIndex = alphabetUnparsed.indexOf("\t");
            String[] alphabet = alphabetUnparsed.substring(tabIndex + 1).split("\t");

            int stateCount = 0;
            String currLine = scan.nextLine();
            while (!currLine.equalsIgnoreCase("--Tokens")) {
                String stateTransitions = currLine;
                tabIndex = stateTransitions.indexOf("\t");
                String state = stateTransitions.substring(0, tabIndex);
                String transitions = stateTransitions.substring(tabIndex + 1);
                statesTransitions.put(state, transitions.split("\t"));
                statesToBeParsed.add(state);
                stateCount++;
                currLine = scan.nextLine();
            }

            HashMap<String, String[]> tokenMap = new HashMap<String, String[]>();
            while (scan.hasNext()) {
                String stateTokens = scan.nextLine();
                tabIndex = stateTokens.indexOf("\t");
                if(tabIndex == -1) {
                    tokenMap.put(stateTokens, new String[0]);
                    continue;
                }
                String state = stateTokens.substring(0, tabIndex);
                String tokenString = stateTokens.substring(tabIndex + 1);
                String[] tokens = tokenString.split("\t");
                tokenMap.put(state, tokens);
            }

            State startState = new State();
            stringToStateMap.put(startStateString, startState);
            MapBasedDFA returnDFA = new MapBasedDFA(startState);

            searchAndAddStates(startStateString, statesToBeParsed, stringToStateMap, statesTransitions, alphabet, finalStateSet, returnDFA);

            for(String currStateString: tokenMap.keySet()) {
                State currState = stringToStateMap.get(currStateString);
                Stack<Token> tokens = new Stack<Token>();
                for(int i = tokenMap.get(currStateString).length-1; i >= 0; i--) {
                    String tokenString = tokenMap.get(currStateString)[i];
                    tokenString = tokenString.substring(1, tokenString.length()-1);
                    if(tokenString.charAt(0) == '/') {
                        tokens.push(new Token(tokenString.substring(1), false));
                    } else {
                        tokens.push(new Token(tokenString, true));
                    }                
                }
                currState.setTokens(tokens);
            }

            return returnDFA;
        } catch (IOException ex) {
            System.out.println("Error writing DFA to: " + file.toString());
            return null;
        }
    }

    private static void searchAndAddStates(String currStateString, List<String> statesToBeParsed, HashMap<String, State> stringToStateMap, HashMap<String, String[]> statesTransitions, String[] alphabet, Set<String> finalStates, MapBasedDFA dfa) {
        if (!statesToBeParsed.contains(currStateString)) return;

        State currState = stringToStateMap.get(currStateString);
        statesToBeParsed.remove(currStateString);
        for (int i = 0; i < statesTransitions.get(currStateString).length; i++) {
            String nextStateString = statesTransitions.get(currStateString)[i];
            Character transitionLetter = alphabet[i].charAt(0);
            State nextState;
            if (stringToStateMap.containsKey(nextStateString)) {
                nextState = stringToStateMap.get(nextStateString);
            } else {
                nextState = new State();
                stringToStateMap.put(nextStateString, nextState);
            }
            if (finalStates.contains(nextStateString)) nextState.setFinal(true);
            dfa.addTransition(currState, transitionLetter, nextState);

            searchAndAddStates(nextStateString, statesToBeParsed, stringToStateMap, statesTransitions, alphabet, finalStates, dfa);
        }
    }
}
