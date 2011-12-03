package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class LLParser {
	
	Map<RULE_TYPE, Map<TOKEN_TYPE, RULE_NUMER>> parseTable;
	
	
	List<String> identifiers;
	List<String> regexes;
	Stack<TOKEN_TYPE> parseStack;
	Stack<String> inputStack;
	
	Map<RULE_NUMER, List<TOKEN_TYPE>> grammarDef;

	enum TOKEN_TYPE{ID, REPLACE, RECURSIVEREPLACE, REGEX, WITH, ASCII_STR, IN, EMPTY, MAXFREQSTRING,
					LEFT_PAREN, RIGHT_PAREN, HASH_TAG, EXC_MARK, DIFF, UNION, INTERS, FIND, PRINT, COMMA, 
					EQUAL_SIGN, SEMI_COLON, BEGIN, END,
					//Grammar starts here 
					MINIRE, STATEMENT_LIST, STATEMENT_LIST_TAIL, STATEMENT, ID_STATEMENT,
					FILE_NAMES, SOURCE_FILE, DESTINATION_FILE, EXP_LIST, EXP_LIST_TAIL, EXP, EXP_TAIL, TERM,
					FILENAME, BIN_OP};
	
	
					enum RULE_TYPE{MINIRE, STATEMENT_LIST, STATEMENT_LIST_TAIL, STATEMENT, ID_STATEMENT,
		FILE_NAMES, SOURCE_FILE, DESTINATION_FILE, EXP_LIST, EXP_LIST_TAIL, EXP, EXP_TAIL, TERM,
		FILENAME, BIN_OP};

					
					
	enum RULE_NUMER{MINIRE, STATEMENT_LIST, STATEMENT_LIST_TAIL1, STATEMENT_LIST_TAIL2, STATEMENT1, ID_STATEMENT1,
					ID_STATEMENT2, ID_STATEMENT3, STATEMENT2, STATEMENT3, FILE_NAMES, SOURCE_FILE,
					DESTINATION_FILE, STATEMENT4, EXP_LIST, EXP_LIST_TAIL, EXP1, EXP2, EXP3, EXP_TAIL1,
					EXP_TAIL2, TERM, FILENAME, BIN_OP1, BIN_OP2, BIN_OP3};

	
	
	public LLParser(List<String> ids, List<String>regex, Stack<String> input){
		identifiers = ids; 
		this.regexes = regex;
		this.inputStack = input;
		parseStack = new Stack<TOKEN_TYPE>();
		parseStack.push(TOKEN_TYPE.MINIRE);
		generateLLTable();
		
	}
	
	private void generateLLTable(){
		initializeGrammarRule();
		
		
		
	
	}
	
	
	
	private void initializeGrammarRule(){
	grammarDef = new HashMap<RULE_NUMER, List<TOKEN_TYPE>>();
	
//		//MINIRE 
//		List<TOKEN_TYPE> tempRules = new ArrayList<TOKEN_TYPE>();
//		tempRules.add(TOKEN_TYPE.STATEMENT_LIST);
//		grammarDef.put(RULE_NUMER.MINIRE, tempRules);
//		//statementList
//		tempRules = new ArrayList<TOKEN_TYPE>();
//		tempRules.add(TOKEN_TYPE.STATEMENT);
//		tempRules.add(TOKEN_TYPE.STATEMENT_LIST_TAIL);
//		grammarDef.put(RULE_NUMER.STATEMENT_LIST, tempRules);
//		//statement-list-tail1
//		tempRules = new ArrayList<TOKEN_TYPE>();
//		tempRules.add(TOKEN_TYPE.STATEMENT);
//		tempRules.add(TOKEN_TYPE.STATEMENT_LIST_TAIL);
//		grammarDef.put(RULE_NUMER.STATEMENT_LIST_TAIL1, tempRules);
//		//statement-list-tail2
//		tempRules = new ArrayList<TOKEN_TYPE>();
//		tempRules.add(TOKEN_TYPE.EMPTY);
//		grammarDef.put(RULE_NUMER.STATEMENT_LIST_TAIL2, tempRules);
		
		try{
		
			FileReader file = new FileReader("grammar_rule.txt");
	    	BufferedReader reader = new BufferedReader(file);
	    	String line;
	    	while((line = reader.readLine()) != null){
	    		String [] tokens = line.split(" ");
	    		RULE_NUMER ruleNum = LLParserHelper.getRuleNumber(tokens[0]);
	    		List<TOKEN_TYPE> rules = new ArrayList<TOKEN_TYPE>();
	    		for(int i=1; i<tokens.length; i++){
	    			TOKEN_TYPE tokenType = LLParserHelper.getTokenType(tokens[i]);
	    			rules.add(tokenType);
	    		}
	    		grammarDef.put(ruleNum, rules);
	    	}
	    	
	    	
		}catch(IOException e){
			e.printStackTrace();
		}
			
		
	}
	
	
	

	
public String rulesToString(){
	StringBuilder sb = new StringBuilder();
	for(RULE_NUMER ruleNum : grammarDef.keySet()){
		List<TOKEN_TYPE> tokens = grammarDef.get(ruleNum);
		sb.append("<" + ruleNum.toString() + ">   ");
		for(TOKEN_TYPE token : tokens){
			sb.append(token.toString()+ " ");
		}
		sb.append("\n");
	}
	return sb.toString();
}
	
	
	
	
	
private TOKEN_TYPE getTokenType(String token){
		
		if(identifiers.contains(token)){
			return TOKEN_TYPE.ID;
		}
		else if(regexes.contains(token)){
			return TOKEN_TYPE.REGEX;
		}
		else if(token.equals("replace")){
			return TOKEN_TYPE.REPLACE;
		}
		else if(token.equals("recursivereplace")){
			return TOKEN_TYPE.RECURSIVEREPLACE;
		}
		else if(token.equals("maxfreqstring")){
			return TOKEN_TYPE.MAXFREQSTRING;
		}
		else if(token.equals("(")){
			return TOKEN_TYPE.LEFT_PAREN;
		}
		else if(token.equals(")")){
			return TOKEN_TYPE.RIGHT_PAREN;
		}
		else if(token.equals("find")){
			return TOKEN_TYPE.FIND;
		}
		else if(token.equals("with")){
			return TOKEN_TYPE.WITH;
		}
		else if(token.equals("!")){
			return TOKEN_TYPE.EXC_MARK;
		}
		else if(token.equals("diff")){
			return TOKEN_TYPE.DIFF;
		}
		else if(token.equals("union")){
			return TOKEN_TYPE.UNION;
		}
		else if(token.equals("inters")){
			return TOKEN_TYPE.INTERS;
		}
		else if(token.equals("in")){
			return TOKEN_TYPE.IN;
		}
		else if(token.equals("#")){
			return TOKEN_TYPE.HASH_TAG;
		}
		else if(token.equals(",")){
			return TOKEN_TYPE.COMMA;
		}
		
		else if(token.equals("=")){
			return TOKEN_TYPE.EQUAL_SIGN;
		}
		
		else if(token.equals(";")){
			return TOKEN_TYPE.SEMI_COLON;
		}
		else if(token.equals("begin")){
			return TOKEN_TYPE.BEGIN;
		}
		
		else if(token.equals("end")){
			return TOKEN_TYPE.END;
		}
		
		
		else{
			return TOKEN_TYPE.ASCII_STR;
		}
		
	}
	

	
}
