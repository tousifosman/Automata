package automata;

import java.util.Set;

/**
 * An object used to define a character class.
 * @author 
 */
public class CharToken {
	String name;
	
        /**
         * The set of characters comprising the definition portion.
         */
        public Set<Character> chars;
        
        /**
         * Name of character class as given in the original specification.
         * @return Name.
         */
        public String name() {
            return name;
        }
        
        /**
         * The set of characters comprising the definition portion.
         * @return A set of characters.
         */
        public Set<Character> chars() {
            return chars;
        }
	
        /**
         * 
         * @param name Name of character class as given in the original specification.
         * @param chars The set of characters comprising the definition portion.
         */
	public CharToken(String name, Set<Character> chars){
		this.name= name;
		
		this. chars = chars;
	
	}
}
