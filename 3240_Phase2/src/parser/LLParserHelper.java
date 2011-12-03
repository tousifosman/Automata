package parser;

import parser.LLParser.RULE_NUMER;
import parser.LLParser.TOKEN_TYPE;

public class LLParserHelper {
	

	public static RULE_NUMER getRuleNumber(String tokenName){
		if(tokenName.contains("MiniRE-program")){
			return RULE_NUMER.MINIRE;
		}
		else if(tokenName.contains("statement-list-tail1")){
			return RULE_NUMER.STATEMENT_LIST_TAIL1;
		}
		else if(tokenName.contains("statement-list-tail2")){
			return RULE_NUMER.STATEMENT_LIST_TAIL2;
		}
		else if(tokenName.contains("statement-list")){
			return RULE_NUMER.STATEMENT_LIST;
		}
		else if(tokenName.contains("statement1")){
			return RULE_NUMER.STATEMENT1;
		}
		else if(tokenName.contains("statement2")){
			return RULE_NUMER.STATEMENT2;
		}
		else if(tokenName.contains("statement3")){
			return RULE_NUMER.STATEMENT3;
		}
		else if(tokenName.contains("statement4")){
			return RULE_NUMER.STATEMENT4;
		}
		else if(tokenName.contains("id-exp1")){
			return RULE_NUMER.ID_STATEMENT1;
		}
		else if(tokenName.contains("id-exp2")){
			return RULE_NUMER.ID_STATEMENT2;
		}
		else if(tokenName.contains("id-exp3")){
			return RULE_NUMER.ID_STATEMENT3;
		}
		else if(tokenName.contains("file-names")){
			return RULE_NUMER.FILE_NAMES;
		}
		else if(tokenName.contains("source-file")){
			return RULE_NUMER.SOURCE_FILE;
		}
		
		else if(tokenName.contains("destination-file")){
			return RULE_NUMER.DESTINATION_FILE;
		}
		
		else if(tokenName.contains("exp-list-tail")){
			return RULE_NUMER.EXP_LIST_TAIL;
		}
		
		else if(tokenName.contains("exp-tail1")){
			return RULE_NUMER.EXP_TAIL1;
		}
		else if(tokenName.contains("exp-tail2")){
			return RULE_NUMER.EXP_TAIL2;
		}
		
		else if(tokenName.contains("exp-list")){
			return RULE_NUMER.EXP_LIST;
		}

		else if(tokenName.contains("exp1")){
			return RULE_NUMER.EXP1;
		}
		else if(tokenName.contains("exp2")){
			return RULE_NUMER.EXP2;
		}
		else if(tokenName.contains("exp3")){
			return RULE_NUMER.EXP3;
		}

		else if(tokenName.contains("term")){
			return RULE_NUMER.TERM;
		}
		else if(tokenName.contains("file-name")){
			return RULE_NUMER.FILENAME;
		}
		else if(tokenName.contains("bin-op1")){
			return RULE_NUMER.BIN_OP1;
		}
		else if(tokenName.contains("bin-op2")){
			return RULE_NUMER.BIN_OP2;
		}
		else if(tokenName.contains("bin-op3")){
			return RULE_NUMER.BIN_OP3;
		}
		else return null;
	}
	
	
public static TOKEN_TYPE getTokenType(String token){
		
		if(token.contains("ID")){
			return TOKEN_TYPE.ID;
		}
		else if(token.contains("REGEX")){
			return TOKEN_TYPE.REGEX;
		}
		else if(token.contains("empty")){
			return TOKEN_TYPE.EMPTY;
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
		
		
		else if (token.contains("MiniRE-program")){
			return TOKEN_TYPE.MINIRE;
		}
		
		else if(token.contains("statement-list-tail")){
			return TOKEN_TYPE.STATEMENT_LIST_TAIL;
		}
		
		else if(token.contains("statement-list")){
			return TOKEN_TYPE.STATEMENT_LIST;
		}
		


		else if(token.contains("statement")){
			return TOKEN_TYPE.STATEMENT;
		}
		else if(token.contains("id-exp")){
			return TOKEN_TYPE.ID_STATEMENT;
		}
		
		else if(token.contains("file-names")){
			return TOKEN_TYPE.FILE_NAMES;
		}
		else if(token.contains("source-file")){
			return TOKEN_TYPE.SOURCE_FILE;
		}
		
		else if(token.contains("destination-file")){
			return TOKEN_TYPE.DESTINATION_FILE;
		}
		else if(token.contains("exp-list-tail")){
			return TOKEN_TYPE.EXP_LIST_TAIL;
		}
		else if(token.contains("exp-list")){
			return TOKEN_TYPE.EXP_LIST;
		}
	
		else if(token.contains("exp-tail")){
			return TOKEN_TYPE.EXP_TAIL;
		}
		else if(token.contains("exp")){
			return TOKEN_TYPE.EXP;
		}
		
		else if(token.equals("end")){
			return TOKEN_TYPE.END;
		}
	
		else if(token.contains("term")){
			return TOKEN_TYPE.TERM;
		}
		else if(token.contains("file-name")){
			return TOKEN_TYPE.FILENAME;
		}
		else if(token.contains("bin-op")){
			return TOKEN_TYPE.BIN_OP;
		}
		
		
		
		
		
		else{
			return TOKEN_TYPE.ASCII_STR;
		}
		
	}

	
	
}
