package automata;

import java.util.Set;

public class CharToken {
	String name;
	Set<Character> chars;
        
        public String name() {
            return name;
        }
        
        public Set<Character> chars() {
            return chars;
        }
	
	public CharToken(String name, Set<Character> chars){
		this.name= name;
		
		this. chars = chars;
	
	}
}
