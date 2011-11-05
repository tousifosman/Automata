import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class RecursiveDescent {


	public static String[] RE_CHAR = {"\\ ", "!", "\\\"", "#", "$", "%", "&", "\\'", "\\(", "\\)", "\\*", "\\+", ",", "-", "\\.", 
		"/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "\\?", "@", "A", "B",
		"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
		"X", "Y", "Z", "\\[", "\\\\", "\\]", "^", "_", "`", "a", "b","c", "d", "e", "f", "g", "h", "i", "j", "k", "l", 
		"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "\\|", "}", "~"}; 
	
	
	public static String[] CLS_CHAR = {" ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "\\-", ".", 
		"/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "@", "A", "B",
		"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
		"X", "Y", "Z", "\\[", "\\\\", "\\]", "^", "_", "`", "a", "b","c", "d", "e", "f", "g", "h", "i", "j", "k", "l", 
		"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|", "}", "~"}; 
	
	//public static String[] OPERATORS = {"|", "*", "+", ".", "^"};
	List<Token> ReCharTokens;
	//List<Token> OperatroList;
	List<String> reCharList;
	List<String> clsCharList;
	List<String> definedClassesNames;
	Map<String, CharToken> definedClasses;
	TempScanner scanner;
	
	public RecursiveDescent(String fileName){
		//TODO JW initialize the scanner;
//		ReCharTokens = new ArrayList<Token>();
//		for(int i=0; i<RE_CHAR.length; i++){
//			ReCharTokens.add(new Token(RE_CHAR[i], false));
//		}
		
		reCharList = Arrays.asList(RE_CHAR);
		clsCharList = Arrays.asList(CLS_CHAR);
		
		//JW TODO replace with real defined classes
		definedClasses = new HashMap<String, CharToken>();	
		definedClassesNames = new ArrayList<String>();
		//JW TODO replace with real scanner
		scanner = new TempScanner();
		
		
		
		
//		OperatroList = new ArrayList<Token>();
//		for(int i=0; i< OPERATORS.length; i++){
//			OperatroList.add(new Token(OPERATORS[i], false));
//			
//		}
		
		
	}
	
	public RecursiveDescentInterState regex() throws SyntaxErrorException{
		RecursiveDescentInterState rexpState = rexp();
		return rexpState;
	}
	
	private RecursiveDescentInterState rexp() throws SyntaxErrorException{
		RecursiveDescentInterState rexp1State = rexp1();
		RecursiveDescentInterState rexpPrimeState = rexpPrime();
		RecursiveDescentInterState interState = unionStates(rexp1State, rexpPrimeState);
		return interState;
	}

	private RecursiveDescentInterState rexpPrime() throws SyntaxErrorException{
		Token token = scanner.peek(); 
		if(token.equals(new Token("|", false))){
			scanner.matchToken(token);
			RecursiveDescentInterState rexp1State = rexp1();
			RecursiveDescentInterState rexpPrimeState =  rexpPrime();
			RecursiveDescentInterState interState = concaInterStates(rexp1State, rexpPrimeState);
			return interState;	
		}
		else{
			return null;
		}
	}
	
	private RecursiveDescentInterState rexp1() throws SyntaxErrorException{
		RecursiveDescentInterState rexp2State = rexp2();
		RecursiveDescentInterState rexp1PrimeState = rexp1Prime();
		RecursiveDescentInterState interState = concaInterStates(rexp2State, rexp1PrimeState);
		return interState;
	}
	

	
	private RecursiveDescentInterState rexp1Prime(){
		RecursiveDescentInterState rexp2State; 
		try{
			rexp2State= rexp2();
		}catch (SyntaxErrorException e) {
			return null;
		}
		RecursiveDescentInterState rexp1PrimeState = rexp1Prime();
		RecursiveDescentInterState interState = concaInterStates(rexp2State, rexp1PrimeState);
		return interState;
	
	}
	
	private RecursiveDescentInterState rexp2()throws SyntaxErrorException{
		Token token= scanner.peek();
		if(token.equals(new Token("(", false))){
			scanner.matchToken(token);
			RecursiveDescentInterState rexpState = rexp();
			scanner.matchToken(new Token(")", false));
			RecursiveDescentInterState rexp2TailState =rexp2Tail();
			
			if(rexp2TailState == null){
				String newRegexString = "(" + rexpState.getCurrentRegex()+")";
				RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, rexpState.getCurrentNFA());
				return interState;
			}
			else if(rexp2TailState.getCurrentRegex().equals("*")){
				MapBasedNFA nfa = (MapBasedNFA) rexpState.getCurrentNFA();
				State startState = nfa.startState();
				startState.setFinal(true);
				Set<State> finalStates = nfa.finalStates();
				for(State fState : finalStates){
					nfa.addTransisition(fState, null, startState);
				}
				String newRegex = rexpState.getCurrentRegex()+"*";
				RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegex, nfa);
				return interState;
				
			}
			else{
				MapBasedNFA nfa = (MapBasedNFA) rexpState.getCurrentNFA();
				State startState = nfa.startState();
				Set<State> finalStates = nfa.finalStates();
				for(State fState:finalStates){
					nfa.addTransisition(fState, null, startState);
				}
				String newRegex = rexpState.getCurrentRegex()+"+";
				RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegex, nfa);
				return interState;
				
			}	
		}
		
		else if(reCharList.contains(token.getValue())){
			scanner.matchToken(token);
			
			RecursiveDescentInterState rexp2TailState = rexp2Tail();
			
			State startState = new State();
			MapBasedNFA nfa = new MapBasedNFA(startState);
			State finalState = new State();
			finalState.setFinal(true);
			Character c = getCharFromString(token.getValue());
			
			if(rexp2TailState == null){
				nfa.addTransisition(startState, c, finalState);
				String newRegex = Character.toString(c);
				RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegex, nfa);
				return interState;
			}
			else if(rexp2TailState.getCurrentRegex().equals("*")){
				startState.setFinal(true);
				nfa.addTransisition(startState, c, finalState);
				nfa.addTransisition(finalState, null, startState);
				String newRegex = Character.toString(c)+"*";
				RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegex, nfa);
				return interState;
			}
			else{
				nfa.addTransisition(startState, c, finalState);
				nfa.addTransisition(finalState, null, startState);
				String newRegex = Character.toString(c)+"+";
				RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegex, nfa);
				return interState;
			}
		}
		else{
			return rexp3();
		}	
			
	}
	
	private RecursiveDescentInterState rexp2Tail() throws SyntaxErrorException{
		Token token = scanner.peek();
		if(token.equals(new Token("*", false))){
			scanner.matchToken(token);
			RecursiveDescentInterState interState= new RecursiveDescentInterState("*", null);
			return interState;
		}
		else if(token.equals(new Token("+", false))){
			scanner.matchToken(token);
			RecursiveDescentInterState interState= new RecursiveDescentInterState("+", null);
			return interState;
		}
		else{
			return null;
		}
	}
	
	private RecursiveDescentInterState rexp3(){
		try{
			return charClass();
		}catch (SyntaxErrorException e) {
			System.out.println("<char-class> failed at <rexp3>\nreturning null");
			return null;
		}
		
	}

	private RecursiveDescentInterState charClass() throws SyntaxErrorException{
		Token token = scanner.peek();
		if(token.equals(new Token(".", false))){
			scanner.matchToken(token);
			Set<Character> allChars = new HashSet<Character>();
			for(String s : reCharList){
				Character c = getCharFromString(s);
				allChars.add(c);
			}
			for(String s : clsCharList){
				Character c = getCharFromString(s);
				allChars.add(c);
			}
			for(String className : definedClassesNames){
				CharToken charClass = definedClasses.get(className);
				for(Character c : charClass.chars){
					allChars.add(c);
				}
			}
			State startState = new State();
			MapBasedNFA nfa = new MapBasedNFA(startState);
			State finalState = new State();
			finalState.setFinal(true);
			
			
			for(Character c: allChars){
				nfa.addTransisition(startState, c, finalState);
			}
			String newRegexString = ".";
			
			RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
			
			return interState;
			
		}
		else if(token.equals(new Token("[", false))){
			scanner.matchToken(token);
			RecursiveDescentInterState charClass1State = charClass1();
			String newRegexString = "[" + charClass1State.getCurrentRegex();
			RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, charClass1State.getCurrentNFA());
			return interState;
		}
		else if(definedClassesNames.contains(token.getValue())){
			scanner.matchToken(token);
			State startState = new State();
			MapBasedNFA nfa = new MapBasedNFA(startState);
			State finalState = new State();
			finalState.setFinal(true);
			
			Set<Character> transitions= definedClasses.get(token.getValue()).chars;
			
			for(Character c : transitions){
				nfa.addTransisition(startState, c, finalState);
			}
			RecursiveDescentInterState interState = new RecursiveDescentInterState(token.getValue(), nfa);
			return interState;	
			
		}
		else{
			throw new SyntaxErrorException();
		}
	}
	
	
	
	private RecursiveDescentInterState charClass1() throws SyntaxErrorException{
		try{
			RecursiveDescentInterState charSetListState = charSetList();
			return charSetListState;
		}catch (SyntaxErrorException e){
			System.out.println("<char-set-list> failed at <char-set>\ntrying to return <exclude-set>");
			RecursiveDescentInterState excludeSetState = excludeSet();
			return excludeSetState;
		}
	
		
	}
	
	private RecursiveDescentInterState charSetList() throws SyntaxErrorException{
		Token token = scanner.peek();
		if(token.equals(new Token("]", false))){
			scanner.matchToken(token);
			String newRegexString  = "]";
			RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, null);
			return interState;
		}
		else{
			RecursiveDescentInterState charSetState= charSet();
			RecursiveDescentInterState charSetListState= charSetList();
			RecursiveDescentInterState newInterState = concaInterStates(charSetState, charSetListState);
			return newInterState;
		}
	}
	
	private RecursiveDescentInterState charSet() throws SyntaxErrorException{
		Token token = scanner.peek();
		if(clsCharList.contains(token.getValue())){
			scanner.matchToken(token);
			RecursiveDescentInterState charSetTailState = charSetTail();
			if(charSetTailState == null){
				Character c = getCharFromString(token.getValue());
				
				State startState = new State();
				MapBasedNFA nfa = new MapBasedNFA(startState);
				State finalState = new State();
				finalState.setFinal(true);
				
				nfa.addTransisition(startState, c, finalState);
				
				String newRegexString = token.getValue();
				RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
				return interState;
			}
			else{
				
				int startIndex = clsCharList.indexOf(token.getValue());
				Character endChar = (Character) charSetTailState.getCurrentNFA().alphabet().toArray()[0];
				int endIndex = clsCharList.indexOf(Character.toString(endChar));
				List<Character> chars = new ArrayList<Character>();
				for(int i=startIndex; i<endIndex; i++){
					Character c = getCharFromString(clsCharList.get(i));
					chars.add(c);
				}
				
				State startState = new State();
				MapBasedNFA nfa = new MapBasedNFA(startState);
				State finalState = new State();
				finalState.setFinal(true);
				for(Character c : chars){
					nfa.addTransisition(startState, c, finalState);
				}
				
				String newRegexString  = token.getValue() + charSetTailState.getCurrentRegex();
				RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
				return interState;
				
			}
			
		
		}
		else{
			throw new SyntaxErrorException();
		}
	}
	
	
	private RecursiveDescentInterState charSetTail() throws SyntaxErrorException{
		Token token = scanner.peek();
		if(token.equals(new Token("-", false))){
			scanner.matchToken(token);
			Token token1 = scanner.peek();
			if(clsCharList.contains(token.getValue())){
				scanner.matchToken(token1);
				
				Character c = getCharFromString(token1.getValue());
				
				
				State startState = new State();
				MapBasedNFA nfa = new MapBasedNFA(startState);
				State finalState = new State(); 
				finalState.setFinal(true);
				
				nfa.addTransisition(startState, c, finalState);
				
				String newRegexString  = "-"+ Character.toString(c);
				RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
				return interState;	
			}
			else{
				throw new SyntaxErrorException();
			}
		}
		else{
			return null;
		}
	}
	
	
	private RecursiveDescentInterState excludeSet() throws SyntaxErrorException{
		scanner.matchToken(new Token("^", false));
		RecursiveDescentInterState charSetSate = charSet();
		scanner.matchToken(new Token("IN", false));
			
		Set<Character> toRemoveChars = charSetSate.getCurrentNFA().alphabet();		
		RecursiveDescentInterState exSetTailState = excludetSetTail();
		Set<Character> allChars = exSetTailState.getCurrentNFA().alphabet();
		
		for(Character c : toRemoveChars){
			allChars.remove(c);
		}
		
		State startState = new State();
		MapBasedNFA nfa = new MapBasedNFA(startState);
		State finalState = new State();
		
		for(Character c: allChars){
			nfa.addTransisition(startState, c, finalState);
		}
		String newRegexString  = "^"+charSetSate.getCurrentRegex()+"IN"+exSetTailState.getCurrentRegex();
		RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
		return interState;
	}

	
	
	private RecursiveDescentInterState excludetSetTail() throws SyntaxErrorException{
		Token token = scanner.peek();
		if(token.equals(new Token("[", false))){
			scanner.matchToken(token);
			RecursiveDescentInterState charSetSate = charSet();
			scanner.matchToken(new Token("]", false));
			
			State startState = new State();
			MapBasedNFA nfa = new MapBasedNFA(startState);
			State finalState = new State();
			finalState.setFinal(true);
			
			NFA charSetNFA = charSetSate.getCurrentNFA();
			
			Set<Character> chars = charSetNFA.alphabet();
			
			for(Character c : chars){
				nfa.addTransisition(startState, c, finalState);
			}
			
			String newRegexString = "["+charSetSate.getCurrentRegex()+"]";
			RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, nfa);
			return interState;
		
		}

		else if(definedClassesNames.contains(token.getValue())) {
			scanner.matchToken(token);
			
			State startState = new State();
			MapBasedNFA nfa = new MapBasedNFA(startState);
			State finalState = new State();
			finalState.setFinal(true);
			
			Set<Character> transitions= definedClasses.get(token.getValue()).chars;
			
			for(Character c : transitions){
				nfa.addTransisition(startState, c, finalState);
			}
			RecursiveDescentInterState interState = new RecursiveDescentInterState(token.getValue(), nfa);
			return interState;	
		}
		else{
			throw new SyntaxErrorException();
		}
		
	}
	
	
	private RecursiveDescentInterState concaInterStates(RecursiveDescentInterState state1, RecursiveDescentInterState state2){
		if(state1==null){
			return state2;
		}
		if(state2 == null){
			return state1;
		}
		MapBasedNFA leftNFA = (MapBasedNFA) state1.getCurrentNFA();
		MapBasedNFA rightNFA = (MapBasedNFA) state2.getCurrentNFA();
		if(leftNFA == null){
			String newRegexString = state1.getCurrentRegex()+ state2.getCurrentRegex();
			RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, state2.getCurrentNFA());
			return interState;
		}
		if(rightNFA == null){
			String newRegexString = state1.getCurrentRegex()+ state2.getCurrentRegex();
			RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, state1.getCurrentNFA());
			return interState;
		}
		Set<State> leftFinal = leftNFA.finalStates();
		State rightStartState = rightNFA.startState();
		
		for(State finalState : leftFinal){
			finalState.setFinal(false);
			leftNFA.addTransisition(finalState, null, rightStartState);
		}
		
		
		
		
		
		//TODO JW concatenate States
		
		return null;
	}
	
	
	private RecursiveDescentInterState unionStates(RecursiveDescentInterState state1, RecursiveDescentInterState state2) {
		if(state1==null){
			return state2;
		}
		if(state2 == null){
			return state1;
		}
		MapBasedNFA leftNFA = (MapBasedNFA) state1.getCurrentNFA();
		MapBasedNFA rightNFA = (MapBasedNFA) state2.getCurrentNFA();
		if(leftNFA == null){
			String newRegexString = state1.getCurrentRegex()+ state2.getCurrentRegex();
			RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, state2.getCurrentNFA());
			return interState;
		}
		if(rightNFA == null){
			String newRegexString = state1.getCurrentRegex()+ state2.getCurrentRegex();
			RecursiveDescentInterState interState = new RecursiveDescentInterState(newRegexString, state1.getCurrentNFA());
			return interState;
		}
		
		
		
		
		
		
		
		
		
		
		// TODO JW union States
		return null;
	}
	
	
	
	
	private Character getCharFromString(String charString){
		if(charString == null){
			
			return null;
		}
		
		Character c;
		if(charString.length()>1){
			c = charString.charAt(1);
		}
		else{
			c = charString.charAt(0);
		}
		return c;
	}
	
	
}
