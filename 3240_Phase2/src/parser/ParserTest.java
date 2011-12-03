package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class ParserTest {
	
	public static void main(String [] args) throws MiniREErrorException{
		//LLParser parser = new LLParser(new ArrayList<String>(), new ArrayList<String>(), new Stack<String>());
		
		//System.out.println(parser.tableToString());
		//System.out.println(parser.rulesToString());
	
//		Test Case 1 		
//		String[] idTokens  = {"myID", "myID1"};
//		String[] regexTokens = {};
//		String [] testTokens = {"begin", "myID", "=", "maxfreqstring" , "(", "myID1", ")", ";", "end"};

		
		
//		Test Case 2 
//		String[] idTokens  = {};
//		String[] regexTokens = {"'regex'"};
//		String [] testTokens = {"begin", "replace", "'regex'", "with" , "asdf", "in", "file1", ">!", "file2", ";", "end"};

		
//		Test Case 3
		String[] idTokens  = {"myID", "myID1"};
		String[] regexTokens = {"'regex'"};
		String [] testTokens = {"begin", "replace", "'regex'", "with" , "asdf", "in", "file1", ">!", "file2", ";", 
				"myID", "=", "maxfreqstring" , "(", "myID1", ")", ";", "end"};
		
		
		
		Stack<String> tokenStack = new Stack<String>();
		for(int i=testTokens.length-1; i>=0; i--){
			tokenStack.push(testTokens[i]);
		}
		
		LLParser parser = new LLParser(Arrays.asList(idTokens), Arrays.asList(regexTokens), tokenStack);
		boolean valid = parser.parse();
		System.out.println("Valid: " + valid);
	}
}
