package generateNFA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import tools.RegexScanner;

import automata.CharToken;
import automata.MapBasedNFA;
import automata.NFA;
import automata.State;
import automata.Token;
import exceptions.SyntaxErrorException;

/**
 * A recursive descent parser class built to recognize regular expressions.
 * @author 
 */
public class RecursiveDescent {

    /**
     * A set of regular expression allowable characters.
     */
    public static String[] RE_CHAR = {"\\ ", "!", "\\\"", "#", "$", "%", "&", "\\'", "\\(", "\\)", "\\*", "\\+", ",", "-", "\\.",
        "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "\\?", "@", "A", "B",
        "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
        "X", "Y", "Z", "\\[", "\\\\", "\\]", "^", "_", "`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
        "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "\\|", "}", "~"};
    public static String[] CLS_CHAR = {" ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "\\-", ".",
        "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "@", "A", "B",
        "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
        "X", "Y", "Z", "\\[", "\\\\", "\\]", "^", "_", "`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
        "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|", "}", "~"};
    //public static String[] OPERATORS = {"|", "*", "+", ".", "^"};
    List<Token> ReCharTokens;
    //List<Token> OperatroList;
    List<String> reCharList;
    List<String> clsCharList;
    List<String> definedClassesNames;
    Map<String, CharToken> definedClasses;
    RegexScanner scanner;
    String nfaName;

    /**
     * 
     * @param scanner A regular expression scanner, capable of dispensing tokens.
     * @param definedClasses A Map of defined Character Classes.
     * @param nfaName Friendly name for the NFA.
     */
    public RecursiveDescent(RegexScanner scanner, Map<String, CharToken> definedClasses, String nfaName) {
        reCharList = Arrays.asList(RE_CHAR);
        clsCharList = Arrays.asList(CLS_CHAR);

        this.definedClasses = definedClasses;
        definedClassesNames = new ArrayList<String>(this.definedClasses.keySet());
        this.scanner = scanner;
        this.nfaName = nfaName;
    }

    /**
     * The first step into the recursive descent procedure. This will recursively parse the input from the scanner until a complete NFA is generated.
     * @return A generated NFA according to tokens supplied by the RegEx scanner.
     * @throws SyntaxErrorException 
     */
    public RecursiveDescentInterState regex() throws SyntaxErrorException {
        RecursiveDescentInterState rexpState = rexp();
        MapBasedNFA nfa = (MapBasedNFA) rexpState.getCurrentNFA();
        Token startToken = new Token(nfaName, true);
        Token endToken = new Token(nfaName, false);

        State startState = nfa.startState();
        startState.addToken(startToken);
        
        Stack<Token> endStack = new Stack<Token>();
        endStack.push(endToken);
        
        State nfaFinalState = new State(endStack);
        nfaFinalState.setFinal(true);
        MapBasedNFA endNFA = new MapBasedNFA(nfaFinalState);
        
        RecursiveDescentInterState endInter = new RecursiveDescentInterState("", endNFA);
        
        RecursiveDescentInterState finalNFA = concaInterStates(rexpState, endInter);
          
        return finalNFA;
    }

    private RecursiveDescentInterState rexp() throws SyntaxErrorException {
        RecursiveDescentInterState rexp1State = rexp1();
        RecursiveDescentInterState rexpPrimeState = rexpPrime();
        RecursiveDescentInterState interState = unionStates(rexp1State, rexpPrimeState);
        return interState;
    }

    private RecursiveDescentInterState rexpPrime() throws SyntaxErrorException {
        Token token = scanner.peek();
        if (token == null) {
            return null;
        }
        if (token.equals(new Token("|", false))) {
            scanner.matchToken(token);
            RecursiveDescentInterState rexp1State = rexp1();
            RecursiveDescentInterState rexpPrimeState = rexpPrime();
            RecursiveDescentInterState interState = concaInterStates(rexp1State, rexpPrimeState);
            return interState;
        } else {
            return null;
        }
    }

    private RecursiveDescentInterState rexp1() throws SyntaxErrorException {
        RecursiveDescentInterState rexp2State = rexp2();
        RecursiveDescentInterState rexp1PrimeState = rexp1Prime();
        RecursiveDescentInterState interState = concaInterStates(rexp2State, rexp1PrimeState);
        return interState;
    }

    private RecursiveDescentInterState rexp1Prime() {
        RecursiveDescentInterState rexp2State;
        try {
            rexp2State = rexp2();
        } catch (SyntaxErrorException e) {
            return null;
        }
        RecursiveDescentInterState rexp1PrimeState = rexp1Prime();
        RecursiveDescentInterState interState = concaInterStates(rexp2State, rexp1PrimeState);
        return interState;

    }

    private RecursiveDescentInterState rexp2() throws SyntaxErrorException {
        Token token = scanner.peek();
        if (token == null) {
            throw new SyntaxErrorException();
        }
        if (token.equals(new Token("(", false))) {
            scanner.matchToken(token);
            RecursiveDescentInterState rexpState = rexp();
            scanner.matchToken(new Token(")", false));
            RecursiveDescentInterState rexp2TailState = rexp2Tail();

            if (rexp2TailState == null) {
                String newRegexString = "(" + rexpState.getCurrentRegex() + ")";
                RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, rexpState.getCurrentNFA());
                return interState;
            } else if (rexp2TailState.getCurrentRegex().equals("*")) {
                MapBasedNFA nfa = (MapBasedNFA) rexpState.getCurrentNFA();
                State startState = nfa.startState();
                startState.setFinal(true);
                Set<State> finalStates = nfa.finalStates();
                for (State fState : finalStates) {
                    nfa.addTransition(fState, null, startState);
                }
                String newRegex = "(" + rexpState.getCurrentRegex() + ")*";
                RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegex, nfa);
                return interState;

            } else {
                MapBasedNFA nfa = (MapBasedNFA) rexpState.getCurrentNFA();
                State startState = nfa.startState();
                Set<State> finalStates = nfa.finalStates();
                for (State fState : finalStates) {
                    nfa.addTransition(fState, null, startState);
                }
                String newRegex = "(" + rexpState.getCurrentRegex() + ")+";
                RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegex, nfa);
                return interState;

            }
        } else if (reCharList.contains(token.getValue())) {
            scanner.matchToken(token);

            RecursiveDescentInterState rexp2TailState = rexp2Tail();

            Token start = new Token(nfaName, true);
            //new Token("RE_CHAR", true);
            Token end = new Token(nfaName, false);
            //new Token("RE_CHAR", false);
            Stack<Token> stack = new Stack<Token>();
            stack.push(start);
            stack.push(end);


            State startState = new State();
            MapBasedNFA nfa = new MapBasedNFA(startState);
            State finalState = new State(stack);
            finalState.setFinal(true);
            Character c = getCharFromString(token.getValue());

            if (rexp2TailState == null) {
                nfa.addTransition(startState, c, finalState);
                String newRegex = Character.toString(c);
                RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegex, nfa);
                return interState;
            } else if (rexp2TailState.getCurrentRegex().equals("*")) {
                startState.setFinal(true);
                nfa.addTransition(startState, c, finalState);
                nfa.addTransition(finalState, null, startState);
                String newRegex = Character.toString(c) + "*";
                RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegex, nfa);
                return interState;
            } else {
                nfa.addTransition(startState, c, finalState);
                nfa.addTransition(finalState, null, startState);
                String newRegex = Character.toString(c) + "+";
                RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegex, nfa);
                return interState;
            }
        } else {
            return rexp3();
        }

    }

    private RecursiveDescentInterState rexp2Tail() throws SyntaxErrorException {
        Token token = scanner.peek();
        if (token == null) {
            throw new SyntaxErrorException();
        }
        if (token.equals(new Token("*", false))) {
            scanner.matchToken(token);
            RecursiveDescentInterState interState = new RecursiveDescentInterState("*", null);
            return interState;
        } else if (token.equals(new Token("+", false))) {
            scanner.matchToken(token);
            RecursiveDescentInterState interState = new RecursiveDescentInterState("+", null);
            return interState;
        } else {
            return null;
        }
    }

    private RecursiveDescentInterState rexp3() throws SyntaxErrorException {
        try {
            return charClass();
        } catch (SyntaxErrorException e) {
            //System.out.println("<char-class> failed at <rexp3>\nreturning null");
            throw new SyntaxErrorException();
            //return null;
        }

    }

    private RecursiveDescentInterState charClass() throws SyntaxErrorException {
        Token token = scanner.peek();
        if (token == null) {
            throw new SyntaxErrorException();
        }
        if (token.equals(new Token(".", false))) {
            scanner.matchToken(token);
            Set<Character> allChars = new HashSet<Character>();
            for (String s : reCharList) {
                Character c = getCharFromString(s);
                allChars.add(c);
            }
            for (String s : clsCharList) {
                Character c = getCharFromString(s);
                allChars.add(c);
            }
            for (String className : definedClassesNames) {
                CharToken charClass = definedClasses.get(className);
                for (Character c : charClass.chars) {
                    allChars.add(c);
                }
            }

            Token start = new Token(nfaName, true);
            		//new Token("DOT", true);
            Token end = new Token(nfaName, false);
            //new Token("DOT", false);
            Stack<Token> stack = new Stack<Token>();
            stack.push(start);
            stack.push(end);

            State startState = new State();
            MapBasedNFA nfa = new MapBasedNFA(startState);
            State finalState = new State(stack);
            finalState.setFinal(true);

            for (Character c : allChars) {
                nfa.addTransition(startState, c, finalState);
            }
            String newRegexString = ".";

            RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);

            return interState;

        } else if (token.equals(new Token("[", false))) {
            scanner.matchToken(token);
            RecursiveDescentInterState charClass1State = charClass1();
            String newRegexString = "[" + charClass1State.getCurrentRegex();
            RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, charClass1State.getCurrentNFA());
            return interState;
        } else if (definedClassesNames.contains(token.getValue())) {
            scanner.matchToken(token);


            Token start = new Token(nfaName, true);
            		//new Token(token.getValue(), true);
            Token end = new Token(nfaName, false);
            		//new Token(token.getValue(), false);
            Stack<Token> stack = new Stack<Token>();
            stack.push(start);
            stack.push(end);

            State startState = new State();
            MapBasedNFA nfa = new MapBasedNFA(startState);
            State finalState = new State(stack);
            finalState.setFinal(true);

            Set<Character> transitions = definedClasses.get(token.getValue()).chars;

            for (Character c : transitions) {
                nfa.addTransition(startState, c, finalState);
            }

            RecursiveDescentInterState interState = new RecursiveDescentInterState(token.getValue(), nfa);
            return interState;

        } else {
            throw new SyntaxErrorException();
        }
    }

    private RecursiveDescentInterState charClass1() throws SyntaxErrorException {
        try {
            RecursiveDescentInterState charSetListState = charSetList();
            return charSetListState;
        } catch (SyntaxErrorException e) {
            System.out.println("<char-set-list> failed at <char-set>\ntrying to return <exclude-set>");
            RecursiveDescentInterState excludeSetState = excludeSet();
            return excludeSetState;
        }


    }

    private RecursiveDescentInterState charSetList() throws SyntaxErrorException {

        try {
            RecursiveDescentInterState charSetState = charSet();
            RecursiveDescentInterState charSetListState = charSetList();
            RecursiveDescentInterState newInterState = concaInterStates(charSetState, charSetListState);
            return newInterState;
        } catch (SyntaxErrorException e) {
            Token token = scanner.peek();
            if (token == null) {
                throw new SyntaxErrorException();
            }
            if (token.equals(new Token("]", false))) {
                scanner.matchToken(token);
                String newRegexString = "]";
                RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, null);
                return interState;
            } else {
                throw new SyntaxErrorException();
            }
        }

    }

    private RecursiveDescentInterState charSet() throws SyntaxErrorException {
        Token token = scanner.peek();
        if (token == null) {
            throw new SyntaxErrorException();
        }
        if (clsCharList.contains(token.getValue())) {
            scanner.matchToken(token);
            RecursiveDescentInterState charSetTailState = charSetTail();
            if (charSetTailState == null) {
                Character c = getCharFromString(token.getValue());


                Token start = new Token(nfaName, true);
                		//new Token("CLS_CHAR", true);
                Token end = new Token(nfaName, false);
                		//new Token("CLS_CHAR", false);
                Stack<Token> stack = new Stack<Token>();
                stack.push(start);
                stack.push(end);


                State startState = new State();
                MapBasedNFA nfa = new MapBasedNFA(startState);
                State finalState = new State(stack);
                finalState.setFinal(true);

                nfa.addTransition(startState, c, finalState);

                String newRegexString = token.getValue();
                RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
                return interState;
            } else {

                int startIndex = clsCharList.indexOf(token.getValue());
                Character endChar = (Character) charSetTailState.getCurrentNFA().alphabet().toArray()[0];
                int endIndex = clsCharList.indexOf(Character.toString(endChar)) + 1;
                List<Character> chars = new ArrayList<Character>();
                for (int i = startIndex; i < endIndex; i++) {
                    Character c = getCharFromString(clsCharList.get(i));
                    chars.add(c);
                }

                Token start = new Token(nfaName, true);
                		//Token("CLS_CHAR", true);
                Token end = new Token(nfaName, false);
                		//new Token("CLS_CHAR", false);
                Stack<Token> stack = new Stack<Token>();
                stack.push(start);
                stack.push(end);

                State startState = new State();
                MapBasedNFA nfa = new MapBasedNFA(startState);
                State finalState = new State(stack);
                finalState.setFinal(true);
                for (Character c : chars) {
                    nfa.addTransition(startState, c, finalState);
                }

                String newRegexString = token.getValue() + charSetTailState.getCurrentRegex();
                RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
                return interState;

            }


        } else {
            throw new SyntaxErrorException();
        }
    }

    private RecursiveDescentInterState charSetTail() throws SyntaxErrorException {
        Token token = scanner.peek();
        if (token == null) {
            throw new SyntaxErrorException();
        }
        if (token.equals(new Token("-", false))) {
            scanner.matchToken(token);
            Token token1 = scanner.peek();
            if (clsCharList.contains(token1.getValue())) {
                scanner.matchToken(token1);

                Token start = new Token(nfaName, true);
                		//Token("CLS_CHAR", true);
                Token end = new Token(nfaName, false);
                		//Token("CLS_CHAR", false);
                Stack<Token> stack = new Stack<Token>();
                stack.push(start);
                stack.push(end);

                Character c = getCharFromString(token1.getValue());
                State startState = new State();
                MapBasedNFA nfa = new MapBasedNFA(startState);
                State finalState = new State(stack);
                finalState.setFinal(true);

                nfa.addTransition(startState, c, finalState);

                String newRegexString = "-" + Character.toString(c);
                RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
                return interState;
            } else {
                throw new SyntaxErrorException();
            }
        } else {
            return null;
        }
    }

    private RecursiveDescentInterState excludeSet() throws SyntaxErrorException {
        scanner.matchToken(new Token("^", false));
        RecursiveDescentInterState charSetSate = charSet();
        scanner.matchToken(new Token("IN", false));

        Set<Character> toRemoveChars = charSetSate.getCurrentNFA().alphabet();
        RecursiveDescentInterState exSetTailState = excludetSetTail();
        Set<Character> allChars = exSetTailState.getCurrentNFA().alphabet();


        Token start;
        Token end;
        Stack<Token> stack = new Stack<Token>();



        String allCharRegex = exSetTailState.getCurrentRegex();
        if (allCharRegex.contains("[")) {
            start = new Token(nfaName, true);
            		//Token("CLS_CHAR", true);
            end = new Token(nfaName, false);
            		//Token("CLS_CHAR", false);
        } else {
            start = new Token(allCharRegex, true);
            end = new Token(allCharRegex, false);
        }

        stack.push(start);
        stack.push(end);

        for (Character c : toRemoveChars) {
            allChars.remove(c);
        }

        State startState = new State();
        MapBasedNFA nfa = new MapBasedNFA(startState);
        State finalState = new State(stack);
        finalState.setFinal(true);

        for (Character c : allChars) {
            nfa.addTransition(startState, c, finalState);
        }
        String newRegexString = "^" + charSetSate.getCurrentRegex() + "IN" + exSetTailState.getCurrentRegex();
        RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
        return interState;
    }

    private RecursiveDescentInterState excludetSetTail() throws SyntaxErrorException {
        Token token = scanner.peek();
        if (token == null) {
            throw new SyntaxErrorException();
        }
        if (token.equals(new Token("[", false))) {
            scanner.matchToken(token);
            RecursiveDescentInterState charSetSate = charSet();
            scanner.matchToken(new Token("]", false));

            /*State startState = new State();
            MapBasedNFA nfa = new MapBasedNFA(startState);
            State finalState = new State();
            finalState.setFinal(true);
            
            NFA charSetNFA = charSetSate.getCurrentNFA();
            
            Set<Character> chars = charSetNFA.alphabet();
            
            for(Character c : chars){
            nfa.addTransition(startState, c, finalState);
            }*/


            NFA nfa = charSetSate.getCurrentNFA();
            String newRegexString = "[" + charSetSate.getCurrentRegex() + "]";
            RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
            return interState;

        } else if (definedClassesNames.contains(token.getValue())) {
            scanner.matchToken(token);

            Token start = new Token(nfaName, true);
            		//Token(token.getValue(), true);
            Token end = new Token(nfaName, false);
            		//Token(token.getValue(), false);
            Stack<Token> stack = new Stack<Token>();
            stack.push(start);
            stack.push(end);

            State startState = new State();
            MapBasedNFA nfa = new MapBasedNFA(startState);
            State finalState = new State(stack);
            finalState.setFinal(true);

            Set<Character> transitions = definedClasses.get(token.getValue()).chars;

            for (Character c : transitions) {
                nfa.addTransition(startState, c, finalState);
            }
            RecursiveDescentInterState interState = new RecursiveDescentInterState(token.getValue(), nfa);
            return interState;
        } else {
            throw new SyntaxErrorException();
        }

    }

    private RecursiveDescentInterState concaInterStates(RecursiveDescentInterState state1, RecursiveDescentInterState state2) {

        if (state1 == null) {
            return state2;
        }
        if (state2 == null) {
            return state1;
        }
        MapBasedNFA leftNFA = (MapBasedNFA) state1.getCurrentNFA();
        MapBasedNFA rightNFA = (MapBasedNFA) state2.getCurrentNFA();
        if (leftNFA == null) {
            String newRegexString = state1.getCurrentRegex() + state2.getCurrentRegex();
            RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, state2.getCurrentNFA());
            System.out.println(newRegexString);
            return interState;
        }
        if (rightNFA == null) {
            String newRegexString = state1.getCurrentRegex() + state2.getCurrentRegex();
            RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, state1.getCurrentNFA());
            System.out.println(newRegexString);
            return interState;
        }





        /*
        // start debugging printout
        System.out.println(state1.getCurrentRegex());
        HashMap<State, HashMap<Character, HashSet<State>>> allTransitions = ((MapBasedNFA)(state1.getCurrentNFA())).getTransitions();
        Set<State> allStates = state1.getCurrentNFA().allStates();
        for(State currState : allStates){
        HashMap<Character, HashSet<State>> currentTransitions = allTransitions.get(currState);
        HashSet<Character> charSet = new HashSet<Character>(currentTransitions.keySet());
        for(Character c : charSet){
        HashSet<State> toStates = currentTransitions.get(c);
        for(State toState : toStates){
        String transitionChar;
        if(c==null){
        transitionChar = "null";
        }
        else {
        transitionChar = Character.toString(c);
        }
        System.out.println(currState.getName()+"---" + transitionChar+"--->"+toState.getName());
        }
        }			
        }		
        System.out.println("Final States:");
        Set<State> finalStates = ((MapBasedNFA)(state1.getCurrentNFA())).finalStates();
        for(State s: finalStates){
        System.out.println(s.getName());
        }
        
        
        System.out.println(state2.getCurrentRegex());
        allTransitions = ((MapBasedNFA)(state2.getCurrentNFA())).getTransitions();
        allStates = state2.getCurrentNFA().allStates();
        for(State currState : allStates){
        HashMap<Character, HashSet<State>> currentTransitions = allTransitions.get(currState);
        HashSet<Character> charSet = new HashSet<Character>(currentTransitions.keySet());
        for(Character c : charSet){
        HashSet<State> toStates = currentTransitions.get(c);
        for(State toState : toStates){
        String transitionChar;
        if(c==null){
        transitionChar = "null";
        }
        else {
        transitionChar = Character.toString(c);
        }
        System.out.println(currState.getName()+"---" + transitionChar+"--->"+toState.getName());
        }
        }			
        }		
        System.out.println("Final States:");
        finalStates = ((MapBasedNFA)(state2.getCurrentNFA())).finalStates();
        for(State s: finalStates){
        System.out.println(s.getName());
        }
        // end debugging printout
         */





        //Set<State> newLeftFinals = new HashSet<State>();


        Set<State> leftFinal = leftNFA.finalStates();
        State rightStartState = rightNFA.startState();
        leftNFA.setFinalStates(new HashSet<State>());
        for (State finalState : leftFinal) {
            finalState.setFinal(false);
            leftNFA.addTransition(finalState, null, rightStartState);
        }
        //appending rightNFA to leftNFA
        HashMap<State, HashMap<Character, HashSet<State>>> allTransitions = rightNFA.getTransitions();
        Set<State> allStates = rightNFA.allStates();
        for (State currState : allStates) {
            HashMap<Character, HashSet<State>> currentTransitions = allTransitions.get(currState);
            HashSet<Character> charSet = new HashSet<Character>(currentTransitions.keySet());
            for (Character c : charSet) {
                HashSet<State> toStates = currentTransitions.get(c);
                for (State toState : toStates) {
                    leftNFA.addTransition(currState, c, toState);
                }
            }
        }
        String newRegexString = state1.getCurrentRegex() + state2.getCurrentRegex();
        //System.out.println(newRegexString);
        RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, leftNFA);
        return interState;
    }

    private RecursiveDescentInterState unionStates(RecursiveDescentInterState state1, RecursiveDescentInterState state2) {

        if (state1 == null) {
            return state2;
        }
        if (state2 == null) {
            return state1;
        }
        MapBasedNFA leftNFA = (MapBasedNFA) state1.getCurrentNFA();
        MapBasedNFA rightNFA = (MapBasedNFA) state2.getCurrentNFA();
        if (leftNFA == null) {
            String newRegexString = state1.getCurrentRegex() + state2.getCurrentRegex();
            RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, state2.getCurrentNFA());
            return interState;
        }
        if (rightNFA == null) {
            String newRegexString = state1.getCurrentRegex() + state2.getCurrentRegex();
            RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, state1.getCurrentNFA());
            return interState;
        }

        State leftStartState = leftNFA.startState();
        State rigthStartState = rightNFA.startState();
        leftNFA.addTransition(leftStartState, null, rigthStartState);

        HashMap<State, HashMap<Character, HashSet<State>>> allTransitions = rightNFA.getTransitions();
        Set<State> allStates = rightNFA.allStates();
        for (State currState : allStates) {
            HashMap<Character, HashSet<State>> currentTransitions = allTransitions.get(currState);
            HashSet<Character> charSet = new HashSet<Character>(currentTransitions.keySet());
            for (Character c : charSet) {
                HashSet<State> toStates = currentTransitions.get(c);
                for (State toState : toStates) {
                    leftNFA.addTransition(currState, c, toState);
                }
            }
        }
        String newRegexString = state1.getCurrentRegex() + "|" + state2.getCurrentRegex();
        RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, leftNFA);
        return interState;
    }

    private Character getCharFromString(String charString) {
        if (charString == null) {

            return null;
        }

        Character c;
        if (charString.length() > 1) {
            c = charString.charAt(1);
        } else {
            c = charString.charAt(0);
        }
        return c;
    }
}
