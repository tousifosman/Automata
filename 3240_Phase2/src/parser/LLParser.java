package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ast.AbstractSyntaxTree;
import ast.ExpressionNode;
import ast.StatementNode;

public class LLParser {
	
	Map<TOKEN_TYPE, Map<TOKEN_TYPE, RULE_NUMER>> parseTable;
	
	
	List<String> identifiers;
	List<String> regexes;
	Stack<TOKEN_TYPE> parseStack;
	Deque<String> inputStack;
	
	Stack<ExpressionNode> expressionNodeStack;
	
	
	Map<RULE_NUMER, List<TOKEN_TYPE>> grammarDef;

	enum TOKEN_TYPE{ID, REPLACE, RECURSIVEREPLACE, REGEX, WITH, ASCII_STR, IN, EMPTY, MAXFREQSTRING,
					LEFT_PAREN, RIGHT_PAREN, HASH_TAG, EXC_MARK, DIFF, UNION, INTERS, FIND, PRINT, COMMA, 
					EQUAL_SIGN, SEMI_COLON, BEGIN, END,
					//Grammar starts here 
					MINIRE, STATEMENT_LIST, STATEMENT_LIST_TAIL, STATEMENT, ID_STATEMENT,
					FILE_NAMES, SOURCE_FILE, DESTINATION_FILE, EXP_LIST, EXP_LIST_TAIL, EXP, EXP_TAIL, TERM,
					FILENAME, BIN_OP};
	
					
	enum RULE_NUMER{MINIRE, STATEMENT_LIST, STATEMENT_LIST_TAIL1, STATEMENT_LIST_TAIL2, STATEMENT1, ID_STATEMENT1,
					ID_STATEMENT2, ID_STATEMENT3, STATEMENT2, STATEMENT3, FILE_NAMES, SOURCE_FILE,
					DESTINATION_FILE, STATEMENT4, EXP_LIST, EXP_LIST_TAIL,EXP_LIST_TAIL1, EXP1, EXP2, EXP3, EXP_TAIL1,
					EXP_TAIL2, TERM, FILENAME, BIN_OP1, BIN_OP2, BIN_OP3};

	
	TOKEN_TYPE[] nonTermArray = {TOKEN_TYPE.MINIRE, TOKEN_TYPE.STATEMENT_LIST, TOKEN_TYPE.STATEMENT_LIST_TAIL, 
			TOKEN_TYPE.STATEMENT, TOKEN_TYPE.ID_STATEMENT, TOKEN_TYPE.FILE_NAMES, TOKEN_TYPE.SOURCE_FILE, 
			TOKEN_TYPE.DESTINATION_FILE, TOKEN_TYPE.EXP_LIST, TOKEN_TYPE.EXP_LIST_TAIL, TOKEN_TYPE.EXP, TOKEN_TYPE.EXP_TAIL, 
			TOKEN_TYPE.TERM, TOKEN_TYPE.FILENAME, TOKEN_TYPE.BIN_OP};
					
	TOKEN_TYPE[] termArray = {TOKEN_TYPE.ID, TOKEN_TYPE.REPLACE, TOKEN_TYPE.RECURSIVEREPLACE, TOKEN_TYPE.REGEX,
			TOKEN_TYPE.WITH, TOKEN_TYPE.ASCII_STR, TOKEN_TYPE.IN, TOKEN_TYPE.EMPTY, TOKEN_TYPE.MAXFREQSTRING,
			TOKEN_TYPE.LEFT_PAREN, TOKEN_TYPE.RIGHT_PAREN, TOKEN_TYPE.HASH_TAG, TOKEN_TYPE.EXC_MARK, TOKEN_TYPE.DIFF, 
			TOKEN_TYPE.UNION, TOKEN_TYPE.INTERS, TOKEN_TYPE.FIND, TOKEN_TYPE.PRINT, TOKEN_TYPE.COMMA, 
			TOKEN_TYPE.EQUAL_SIGN, TOKEN_TYPE.SEMI_COLON, TOKEN_TYPE.BEGIN, TOKEN_TYPE.END};
	
	TOKEN_TYPE[] otherArray = {TOKEN_TYPE.SOURCE_FILE, TOKEN_TYPE.DESTINATION_FILE, TOKEN_TYPE.FILE_NAMES}; 
	
	String [] extraTokenArray = {"(", ")", ";", "in", "end", "find", "maxfreqstring", ","};
	
	
	
	List<TOKEN_TYPE> otherTokens = Arrays.asList(otherArray);
	List<TOKEN_TYPE> nonTermTokens = Arrays.asList(nonTermArray);
	List<TOKEN_TYPE> termTokens = Arrays.asList(termArray);
	List<String> extraTokens = Arrays.asList(extraTokenArray);
	
	public LLParser(List<String> ids, List<String>regex, Deque<String> input){
		identifiers = ids; 
		this.regexes = regex;
		this.inputStack = input;
		expressionNodeStack = new Stack<ExpressionNode>();
		parseStack = new Stack<TOKEN_TYPE>();
		parseStack.push(TOKEN_TYPE.MINIRE);
		generateLLTable();
	}
	
	private void generateLLTable(){
		initializeGrammarRule();
		
		parseTable = new HashMap<LLParser.TOKEN_TYPE, Map<TOKEN_TYPE,RULE_NUMER>>();
		
		
		//MINIRE Row
		TOKEN_TYPE currTokenType = TOKEN_TYPE.MINIRE;
		Map<TOKEN_TYPE, RULE_NUMER> tableRow= new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.BEGIN, RULE_NUMER.MINIRE);
		parseTable.put(currTokenType, tableRow);

		//Statment-List Row
		currTokenType = TOKEN_TYPE.STATEMENT_LIST;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.ID, RULE_NUMER.STATEMENT_LIST);
		tableRow.put(TOKEN_TYPE.REPLACE, RULE_NUMER.STATEMENT_LIST);
		tableRow.put(TOKEN_TYPE.RECURSIVEREPLACE, RULE_NUMER.STATEMENT_LIST);
		tableRow.put(TOKEN_TYPE.PRINT, RULE_NUMER.STATEMENT_LIST);
		parseTable.put(currTokenType, tableRow);
		
		
		//Statment-List-Tail Row
		currTokenType = TOKEN_TYPE.STATEMENT_LIST_TAIL;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.ID, RULE_NUMER.STATEMENT_LIST_TAIL1);
		tableRow.put(TOKEN_TYPE.REPLACE, RULE_NUMER.STATEMENT_LIST_TAIL1);
		tableRow.put(TOKEN_TYPE.RECURSIVEREPLACE, RULE_NUMER.STATEMENT_LIST_TAIL1);
		tableRow.put(TOKEN_TYPE.PRINT, RULE_NUMER.STATEMENT_LIST_TAIL1);
		tableRow.put(TOKEN_TYPE.EMPTY, RULE_NUMER.STATEMENT_LIST_TAIL2);
		parseTable.put(currTokenType, tableRow);
		
		//Statment Row
		currTokenType = TOKEN_TYPE.STATEMENT;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.ID, RULE_NUMER.STATEMENT1);
		tableRow.put(TOKEN_TYPE.REPLACE, RULE_NUMER.STATEMENT2);
		tableRow.put(TOKEN_TYPE.RECURSIVEREPLACE, RULE_NUMER.STATEMENT3);
		tableRow.put(TOKEN_TYPE.PRINT, RULE_NUMER.STATEMENT4);
		parseTable.put(currTokenType, tableRow);
		
		//ID-Statement Row
		currTokenType = TOKEN_TYPE.ID_STATEMENT;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.HASH_TAG, RULE_NUMER.ID_STATEMENT2);
		tableRow.put(TOKEN_TYPE.MAXFREQSTRING, RULE_NUMER.ID_STATEMENT3);
		tableRow.put(TOKEN_TYPE.ID, RULE_NUMER.ID_STATEMENT1);
		tableRow.put(TOKEN_TYPE.LEFT_PAREN, RULE_NUMER.ID_STATEMENT1);
		parseTable.put(currTokenType, tableRow);
		
		//FILE_NAMEs Row
		currTokenType = TOKEN_TYPE.FILE_NAMES;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.ASCII_STR, RULE_NUMER.FILE_NAMES);
		parseTable.put(currTokenType, tableRow);
		
		//SOURCE_FILE Row
		currTokenType = TOKEN_TYPE.SOURCE_FILE;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.ASCII_STR, RULE_NUMER.SOURCE_FILE);
		parseTable.put(currTokenType, tableRow);

		//DESTINATION_FILE Row
		currTokenType = TOKEN_TYPE.DESTINATION_FILE;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.ASCII_STR, RULE_NUMER.DESTINATION_FILE);
		parseTable.put(currTokenType, tableRow);
		
		//EXP_LIST Row
		currTokenType = TOKEN_TYPE.EXP_LIST;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.ID, RULE_NUMER.EXP_LIST);
		tableRow.put(TOKEN_TYPE.LEFT_PAREN, RULE_NUMER.EXP_LIST);
		parseTable.put(currTokenType, tableRow);
		
		//EXP_LIST_TAIL Row
		currTokenType = TOKEN_TYPE.EXP_LIST_TAIL;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.COMMA, RULE_NUMER.EXP_LIST_TAIL);
		tableRow.put(TOKEN_TYPE.EMPTY, RULE_NUMER.EXP_LIST_TAIL1);
		parseTable.put(currTokenType, tableRow);
		
		//EXP Row
		currTokenType = TOKEN_TYPE.EXP;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.ID, RULE_NUMER.EXP1);
		//tableRow.put(TOKEN_TYPE.FIND, RULE_NUMER.EXP3);
		//tableRow.put(TOKEN_TYPE.ID, RULE_NUMER.EXP2);
		//tableRow.put(TOKEN_TYPE.LEFT_PAREN, RULE_NUMER.EXP2);
		parseTable.put(currTokenType, tableRow);
		
		//EXP_TAIL Row
		currTokenType = TOKEN_TYPE.EXP_TAIL;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.DIFF, RULE_NUMER.EXP_TAIL1);
		tableRow.put(TOKEN_TYPE.UNION, RULE_NUMER.EXP_TAIL1);
		tableRow.put(TOKEN_TYPE.INTERS, RULE_NUMER.EXP_TAIL1);
		tableRow.put(TOKEN_TYPE.EMPTY, RULE_NUMER.EXP_TAIL2);
		parseTable.put(currTokenType, tableRow);
		
		//TERM Row
		currTokenType = TOKEN_TYPE.TERM;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.LEFT_PAREN, RULE_NUMER.TERM);
		parseTable.put(currTokenType, tableRow);
		
		//FILENAME Row
		currTokenType = TOKEN_TYPE.FILENAME;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.ASCII_STR, RULE_NUMER.FILENAME);
		parseTable.put(currTokenType, tableRow);
			
		//BIN_OP Row
		currTokenType = TOKEN_TYPE.BIN_OP;
		tableRow = new HashMap<LLParser.TOKEN_TYPE, LLParser.RULE_NUMER>();
		tableRow.put(TOKEN_TYPE.DIFF, RULE_NUMER.BIN_OP1);
		tableRow.put(TOKEN_TYPE.UNION, RULE_NUMER.BIN_OP2);
		tableRow.put(TOKEN_TYPE.INTERS, RULE_NUMER.BIN_OP3);
		parseTable.put(currTokenType, tableRow);
		
	}
	
	
	public AbstractSyntaxTree parse() throws MiniREErrorException{
		AbstractSyntaxTree astTree = new AbstractSyntaxTree();
		StatementNode currentStatementNode = null;
		ExpressionNode currentExpressionNode =null;
		
		while(!parseStack.isEmpty()){
			TOKEN_TYPE currentToken = parseStack.peek();
			if(nonTermTokens.contains(currentToken)){
				TOKEN_TYPE lookAheadToken = getTokenType(inputStack.peek());	
				Map<TOKEN_TYPE, RULE_NUMER> currentRow = parseTable.get(currentToken);
				RULE_NUMER newRule;
				if(lookAheadToken.equals(TOKEN_TYPE.LEFT_PAREN) && currentToken.equals(TOKEN_TYPE.EXP)){
					String tempToken = inputStack.pop();
					lookAheadToken = getTokenType(inputStack.peek());
					inputStack.push(tempToken);
					if(lookAheadToken.equals(TOKEN_TYPE.FIND)){
						newRule = RULE_NUMER.EXP3;
					}
					else if(lookAheadToken.equals(TOKEN_TYPE.LEFT_PAREN) || lookAheadToken.equals(TOKEN_TYPE.ID)){
						newRule = RULE_NUMER.EXP2;
					}
					else {
						throw new MiniREErrorException("Exp with LEFT_PAREN "+ " Looking ahead is: " + lookAheadToken.toString());
					}
				}
				else{
					
					if(lookAheadToken == TOKEN_TYPE.END){
						newRule= currentRow.get(TOKEN_TYPE.EMPTY);
					}
					else if(currentToken.equals(TOKEN_TYPE.EXP_TAIL) && lookAheadToken == TOKEN_TYPE.SEMI_COLON){
						newRule= currentRow.get(TOKEN_TYPE.EMPTY);
					}
					else if(currentToken.equals(TOKEN_TYPE.EXP_LIST_TAIL) && lookAheadToken == TOKEN_TYPE.RIGHT_PAREN){
						newRule= currentRow.get(TOKEN_TYPE.EMPTY);
					}
					else{
						newRule = currentRow.get(lookAheadToken);	
					}
					
					if(newRule == null){
						throw new MiniREErrorException("At "+ currentToken.toString() + " Looking ahead is: " + lookAheadToken.toString());
					}
					
				}
				if(TOKEN_TYPE.STATEMENT.equals(currentToken)){
					String type;
					Object value;
					if(newRule.equals(RULE_NUMER.STATEMENT1)){
						type = "assign";
						value = inputStack.peek();
					}
					else if(newRule.equals(RULE_NUMER.STATEMENT2)){
						type = "replace";
						value = new String[4];
					}
					else if(newRule.equals(RULE_NUMER.STATEMENT3)){
						type = "recursivereplace";
						value = new String[4];
					}
					else {
						type = "print";
						value = null;
					}
					if(currentStatementNode == null){
						currentStatementNode = new StatementNode(type, value);
						astTree.setHead(currentStatementNode);
					}
					else{
						StatementNode newStatementNode = new StatementNode(type, value);
						currentStatementNode.setNextNode(newStatementNode);
						currentExpressionNode=null;
						currentStatementNode = newStatementNode;		
					}
					
				}
				//Placing values into node values
				if(currentToken.equals(TOKEN_TYPE.SOURCE_FILE)){
					((String[]) currentStatementNode.value())[2]= inputStack.peek();
				}
				else if(currentToken.equals(TOKEN_TYPE.DESTINATION_FILE)){
					((String[]) currentStatementNode.value())[3]= inputStack.peek();
				}
//				else if(currentToken.equals(TOKEN_TYPE.FILENAME)){
//					((String[]) currentExpressionNode.value())[1]= inputStack.peek();
//				}
				
				
				List<TOKEN_TYPE> newTokens = grammarDef.get(newRule);
				
				
				//Create Expression Nodes
				if(currentToken.equals(TOKEN_TYPE.ID_STATEMENT)){
					currentExpressionNode = new ExpressionNode(TOKEN_TYPE.ID_STATEMENT.toString(), null);
					currentStatementNode.addSubnode(currentExpressionNode);
					expressionNodeStack.push(currentExpressionNode);
					//currentStatementNode = null;
				
				}
				else if(currentToken.equals(TOKEN_TYPE.EXP_LIST)){
					currentExpressionNode = new ExpressionNode(TOKEN_TYPE.EXP_LIST.toString(), null);
					currentStatementNode.addSubnode(currentExpressionNode);
					expressionNodeStack.push(currentExpressionNode);
					//currentStatementNode=null;
				}
				
				//update expression node NEED a new stack for expression Nodes
				TOKEN_TYPE newTokenType = parseStack.pop();
				for(int i=newTokens.size()-1; i>=0; i--){
					parseStack.push(newTokens.get(i));
				}
				if(!(TOKEN_TYPE.STATEMENT.equals(newTokenType) || TOKEN_TYPE.MINIRE.equals(newTokenType)
						|| TOKEN_TYPE.STATEMENT_LIST.equals(newTokenType) || TOKEN_TYPE.STATEMENT_LIST_TAIL.equals(newTokenType))
						&& !(termTokens.contains(newTokenType)) && !(otherTokens.contains(newTokenType))){
					ExpressionNode tempExpressionNode = expressionNodeStack.pop();
					
					if(!tempExpressionNode.type().equals(TOKEN_TYPE.BIN_OP.toString())){
						tempExpressionNode.setType(newRule.toString());
					}

					if(!tempExpressionNode.type().equals(RULE_NUMER.FILENAME.toString())){
						currentExpressionNode = tempExpressionNode;
					}
					
					for(int i=newTokens.size()-1; i>=0; i--){
						ExpressionNode newExpressionNode = new ExpressionNode(newTokens.get(i).toString(), null);
						
						if(nonTermTokens.contains(newTokens.get(i)) && (!otherTokens.contains(newTokens.get(i))) ){
							expressionNodeStack.push(newExpressionNode);
							currentExpressionNode.addSubnode(newExpressionNode);
						}
					}
				}

			}
			else{
				TOKEN_TYPE parseToken = parseStack.peek();
				TOKEN_TYPE lookAheadToken = getTokenType(inputStack.peek());;
				
				if(parseToken.equals(TOKEN_TYPE.EMPTY)){
					parseStack.pop();
				}
				
				else if(!parseToken.equals(TOKEN_TYPE.EMPTY) && parseToken.equals(lookAheadToken)){
					if(currentExpressionNode == null){
						if(currentToken.equals(TOKEN_TYPE.REGEX)){
							((String[]) currentStatementNode.value())[0]= inputStack.peek();
						}
						if(currentToken.equals(TOKEN_TYPE.ASCII_STR)){
							if(((String[]) currentStatementNode.value())[1]==null){
								((String[]) currentStatementNode.value())[1]= inputStack.peek();
							}
						}
					}
					else{
//						if(currentToken.equals(TOKEN_TYPE.REGEX)){
//							((String[]) currentExpressionNode.value())[0]= inputStack.peek();
//						}
						String currentString = inputStack.peek();
						if(!extraTokens.contains(currentString)){
						
							if(currentExpressionNode.value() == null){
								currentExpressionNode.setValue(inputStack.peek());
							}
							else{
								String [] newValue;
								Object oldValue = currentExpressionNode.value();
								if(oldValue instanceof String){
									newValue = new String[2];
									newValue[0] = (String) oldValue;
									newValue[1] = inputStack.peek();
								}
								else{
									String [] interValue = (String[]) oldValue;
									newValue = new String[interValue.length+1];
									int i=0;
									for(; i<interValue.length; i++){
										newValue[i] = interValue[i];
									}
									newValue[i] = inputStack.peek();
									
								}
								currentExpressionNode.setValue(newValue);
							}						
						}
					}
					parseStack.pop();
					inputStack.pop();
				}
				else{
					throw new MiniREErrorException("Terminial Token: "+parseToken.toString() + "    Matching: " + lookAheadToken.toString());
				}
			}
		}
		
		if(inputStack.isEmpty()){
			return astTree;
		}
		else{
			throw new MiniREErrorException("Input stack not empty in the end.");
		}
		
	}
	
	
	
	
	
	
	private void initializeGrammarRule(){
		grammarDef = new HashMap<RULE_NUMER, List<TOKEN_TYPE>>();
		
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
	


public String tableToString(){
	StringBuilder sb = new StringBuilder();
	
	for(TOKEN_TYPE nonToken: nonTermTokens){
		Map<TOKEN_TYPE, RULE_NUMER> map = parseTable.get(nonToken);
		sb.append(nonToken.toString()+"      ");
		if(map != null){
			for(TOKEN_TYPE token : termTokens){
				RULE_NUMER rule = map.get(token);
				String s;
				if(rule != null){
					sb.append(token.toString() + " : " + rule.toString() + "     ");
				}
						
			}
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
		else if(token.equals(">!")){
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
		else if(token.equals("print")){
			return TOKEN_TYPE.PRINT;
		}
		
		else{
			return TOKEN_TYPE.ASCII_STR;
		}
		
	}
	

	
}
