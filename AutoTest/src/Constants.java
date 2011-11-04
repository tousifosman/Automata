


import java.util.Arrays;
import java.util.List;

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
	
}
