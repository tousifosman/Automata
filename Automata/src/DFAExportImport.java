
import automata.DFA;
import automata.MapBasedDFA;
import automata.State;
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
            while (scan.hasNext()) {
                String stateTransitions = scan.nextLine();
                tabIndex = stateTransitions.indexOf("\t");
                String state = stateTransitions.substring(0, tabIndex);
                String transitions = stateTransitions.substring(tabIndex + 1);
                statesTransitions.put(state, transitions.split("\t"));
                statesToBeParsed.add(state);
                stateCount++;
            }

            State startState = new State();
            stringToStateMap.put(startStateString, startState);
            MapBasedDFA returnDFA = new MapBasedDFA(startState);
            
            searchAndAddStates(startStateString, statesToBeParsed, stringToStateMap, statesTransitions, alphabet, finalStateSet, returnDFA);
            

            return returnDFA;
        } catch (IOException ex) {
            System.out.println("Error writing DFA to: " + file.toString());
            return null;
        }
    }

    private static void searchAndAddStates(String currStateString, List<String> statesToBeParsed, HashMap<String, State> stringToStateMap, HashMap<String, String[]> statesTransitions, String[] alphabet, Set<String> finalStates, MapBasedDFA dfa) {
        if(!statesToBeParsed.contains(currStateString)) return;
        
        State currState = stringToStateMap.get(currStateString);
        statesToBeParsed.remove(currStateString);
        for(int i = 0; i < statesTransitions.get(currStateString).length; i++) {
            String nextStateString = statesTransitions.get(currStateString)[i];
            Character transitionLetter = alphabet[i].charAt(0);
            State nextState;
            if(stringToStateMap.containsKey(nextStateString)) {
                nextState = stringToStateMap.get(nextStateString);
            } else {
                nextState = new State();
                stringToStateMap.put(nextStateString, nextState);
            }
            if(finalStates.contains(nextStateString)) nextState.setFinal(true);
            dfa.addTransisition(currState, transitionLetter, nextState);
            
            searchAndAddStates(nextStateString, statesToBeParsed, stringToStateMap, statesTransitions, alphabet, finalStates, dfa);
        }
    }
}
