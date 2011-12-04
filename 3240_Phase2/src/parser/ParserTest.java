package parser;

import java.util.Arrays;
import java.util.Stack;

import ast.AbstractSyntaxTree;
import ast.Node;

public class ParserTest {
	
	public static void main(String [] args) throws MiniREErrorException{
		//LLParser parser = new LLParser(new ArrayList<String>(), new ArrayList<String>(), new Stack<String>());
		
		//System.out.println(parser.tableToString());
		//System.out.println(parser.rulesToString());
	
//		Test Case 1 		
		String[] idTokens  = {"myID", "myID1"};
		String[] regexTokens = {"'regex'"};
		String [] testTokens = {"begin", "myID", "=", "maxfreqstring" , "(", "myID1", ")", ";", 
				"replace", "'regex'", "with" , "asdf", "in", "file1", ">!", "file2", ";","end"};

		
		
//		Test Case 2 
//		String[] idTokens  = {};
//		String[] regexTokens = {"'regex'"};
//		String [] testTokens = {"begin", "replace", "'regex'", "with" , "asdf", "in", "file1", ">!", "file2", ";", "end"};

		
//		Test Case 3
//		String[] idTokens  = {"myID", "myID1"};
//		String[] regexTokens = {"'regex'"};
//		String [] testTokens = {"begin", "replace", "'regex'", "with" , "asdf", "in", "file1", ">!", "file2", ";", 
//				"myID", "=", "maxfreqstring" , "(", "myID1", ")", ";", "end"};
		

//		TestCase 4
//		String[] idTokens  = {"myID", "myID1"};
//		String[] regexTokens = {"'regex'"};
//		String [] testTokens = {"begin", "myID", "=", "(", "find", "'regex'", "in", "file1", ")" , ";", "end"};
//			
		
		
		
		Stack<String> tokenStack = new Stack<String>();
		for(int i=testTokens.length-1; i>=0; i--){
			tokenStack.push(testTokens[i]);
		}
		
		LLParser parser = new LLParser(Arrays.asList(idTokens), Arrays.asList(regexTokens), tokenStack);
		AbstractSyntaxTree tree = parser.parse();
		Node headNode = tree.getHead();
		while(headNode != null){
			System.out.print(headNode.type()+"    ");
			try{
				String[] values = (String[])headNode.value();
				for(int i=0; i<values.length; i++){
					System.out.print(values[i]+ "  " );
					
				}
			}catch(ClassCastException e){
				String value = (String) headNode.value();
				System.out.print(value+ "  " );
			}
			System.out.print("\n");
			headNode= headNode.nextNode();
		}
		
		

	}
}
