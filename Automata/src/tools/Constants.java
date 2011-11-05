package tools;



import java.util.Arrays;
import java.util.List;
import automata.Token;

/**
 * Contains useful Character Class constants and static Tokens.
 * @author 
 */
public class Constants {
	
	public static Character[] lowerCase = {'a', 'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v',
		'w','x','y','z'} ;
	
	public static Character[] upperCase = {'A', 'B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V',
		'W','X','Y','Z'} ;
	
	public static Character [] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	
	public static Character [] operators = {'*','+','(',')','[',']','|', '.'};
 	
	public static Character [] specialChars = {'*','+','(',')','[',']','|', '.', ' ', '\\'};
	
	
	public static List<Character>lowerCaseList = Arrays.asList(lowerCase);
	public static List<Character>upperCaseList = Arrays.asList(upperCase);
	public static List<Character>digitsList = Arrays.asList(digits);
	public static List<Character>operatorList = Arrays.asList(operators);
	public static List<Character>specialCharsList = Arrays.asList(specialChars);
        
        /* Static Tokens for Recognition by Scanner */
        public static Token UNION = new Token("|");
        public static Token EPSILON = new Token("");
        public static Token LPAREN = new Token("(");
        public static Token RPAREN = new Token(")");
        public static Token ASTERISK = new Token("*");
        public static Token PLUS = new Token("+");
        public static Token FULLSTOP = new Token("(");
        public static Token CARAT = new Token("^");
        public static Token LBRACKET = new Token("[");
        public static Token RBRACKET = new Token("]");
        public static Token QUESTION = new Token("?");
	
}
