package parser;

import java.util.ArrayList;
import java.util.Stack;

public class ParserTest {
	
	public static void main(String [] args){
		LLParser parser = new LLParser(new ArrayList<String>(), new ArrayList<String>(), new Stack<String>());
		System.out.println(parser.rulesToString());
	}
}
