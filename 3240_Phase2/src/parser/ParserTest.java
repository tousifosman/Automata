package parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import ast.AbstractSyntaxTree;
import ast.Node;

public class ParserTest {
	
	public static void main(String [] args) throws MiniREErrorException{
		//LLParser parser = new LLParser(new ArrayList<String>(), new ArrayList<String>(), new Stack<String>());
		
		//System.out.println(parser.tableToString());
		//System.out.println(parser.rulesToString());
	
//		Test Case 1 		
		String[] idTokens  = {"match_these", "these", "match_the", "the_size", "the"};
		String[] regexTokens = {"'[A-z a-z]*h[A-z a-z]*'", "'[A-z a-z]*the[A-z a-z]*'", "'[A-z a-z]*s[A-z a-z]*'"};
		String [] testTokens = {"begin", "replace", "'[A-z a-z]*h[A-z a-z]*'", "with", "anana", "in", "input.txt", ">!", "result2.txt", ";",
				"match_these", "=", "(", "find", "'[A-z a-z]*the[A-z a-z]*'", "in", "input.txt", ")", "inters", 
				"(", "find", "'[A-z a-z]*s[A-z a-z]*'", "in", "input.txt", ")", ";", 
				"these", "=", "maxfreqstring" , "(", "match_these", ")", ";",
				"match_the", "=", "(", "find", "'[A-z a-z]*the[A-z a-z]*'", "in", "input.txt", ")", ";",                                 
				"the_size", "=", "#", "match_the", ";",                                 
				"the", "=", "maxfreqstring" , "(", "match_the", ")", ";",
				"print", "(", "match_the", ",", "the_size", ")", ";",
                                "end"};

		
		
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
		
		Queue<Node> nodeQueue = new LinkedList<Node>();
		
		LLParser parser = new LLParser(Arrays.asList(idTokens), Arrays.asList(regexTokens), tokenStack);
		AbstractSyntaxTree tree = parser.parse();
                System.out.println(tree);
                
                
		/*Node headNode = tree.getHead();
		while(headNode != null){
			System.out.print(headNode.type()+"    ");
			if(headNode.value() instanceof String){
				String value = (String) headNode.value();
				System.out.print(value+ "  " );
			}
			else{
				String[] values = (String[])headNode.value();
				for(int i=0; i<values.length; i++){
					System.out.print(values[i]+ "  " );
				}
			}
			System.out.print("\n");
			
			List<Node> subNodes = headNode.subnodes();
			if(null != subNodes){
				System.out.print(headNode.type()+": ");
				for(Node node : subNodes){
					System.out.print(node.type()+": ");
					nodeQueue.add(node);
					if(node.value() instanceof String){
						String value = (String) node.value();
						System.out.print(value+ "  " );
					}
					else{
						String[] values = (String[])node.value();
						if(values != null){
							for(int i=0; i<values.length; i++){
								System.out.print(values[i]+ "  " );
							}
						}
					}
					System.out.print(" | ");
				}
				System.out.print("\n");
			}
			while(!nodeQueue.isEmpty()){
				Node currentNode = nodeQueue.poll();
				System.out.print(currentNode.type()+": ");
				subNodes = currentNode.subnodes();
				if(null != subNodes){
					for(Node node : subNodes){
						System.out.print(node.type()+": ");
						nodeQueue.add(node);
						if(node.value() instanceof String){
							String value = (String) node.value();
							System.out.print(value+ "  " );
						}
						else{
							String[] values = (String[])node.value();
							if(values != null){
								for(int i=0; i<values.length; i++){
									System.out.print(values[i]+ "  " );
								}
							}
						}
						System.out.print(" | ");
					}
					System.out.print("\n");
				}
			}
			System.out.print("\n");
			
			
			headNode= headNode.nextNode();
		}*/
		

	}
}
