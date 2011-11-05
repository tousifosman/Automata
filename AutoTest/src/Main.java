import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;



public class Main {

	/**
	 * @param args
	 * @throws SyntaxErrorException 
	 */
	public static void main(String[] args) throws SyntaxErrorException {
		
		
		//Character[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
//		Character[] smallCases = {'a', 'b','c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 
//				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
//		Character[] letters = {'A', 'B',
//				'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
//				'X', 'Y', 'Z', 'a', 'b','c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 
//				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		
		Character[] digits={'0'};
		Character[] smallCases = {'a'};
		Character[] letters = {'A'};
		
		HashMap<String, CharToken> charClasses = new HashMap<String, CharToken>();
		String digit = "$DIGIT";
		String smallCase = "$SMALLCASE";
		String letter = "$LETTER";
		
		HashSet<Character> digitSet = new HashSet<Character>();
		for(int i=0; i<digits.length; i++){
			digitSet.add(digits[i]);
		}
		
		HashSet<Character> smSet = new HashSet<Character>();
		for(int i=0; i<smallCases.length; i++){
			smSet.add(smallCases[i]);
		}
		
		HashSet<Character> letterSet = new HashSet<Character>();
		for(int i=0; i<letters.length; i++){
			letterSet.add(letters[i]);
		}
		
		
		CharToken digitToken = new CharToken(digit, digitSet);
		charClasses.put(digit, digitToken);
		
		CharToken smToken = new CharToken(smallCase, smSet);
		charClasses.put(smallCase, smToken);
		
		CharToken letterToken = new CharToken(letter, letterSet);
		charClasses.put(letter, letterToken);
		
		TempScanner scanner = new TempScanner();
		
		RecursiveDescent rd = new RecursiveDescent(scanner, charClasses, "$ID");
		RecursiveDescentInterState state = rd.regex();
		
		System.out.println("Final regex: "+state.getCurrentRegex());
		
		
		HashMap<State, HashMap<Character, HashSet<State>>> allTransitions = ((MapBasedNFA)(state.getCurrentNFA())).getTransitions();
		Set<State> allStates = state.getCurrentNFA().allStates();
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
		Set<State> finalStates = ((MapBasedNFA)(state.getCurrentNFA())).finalStates();
		for(State s: finalStates){
			System.out.println(s.getName());
		}
		
		
		System.out.println();

	}
}



