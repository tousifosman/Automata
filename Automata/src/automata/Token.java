package automata;

public class Token {
    private boolean startToken;
    private String value;
    
    public Token(String value, boolean startToken) {
        this.startToken = startToken;
        this.value = value;
    }
    
    /**
     * Token constructor which assumes startToken to be false. Useful for tokens not to be added to an Automaton.
     * @param value The literal value of the token.
     */
    public Token(String value) {
        this(value, false);
    }
    
    public String getValue() {
        return value;
    }
    
    public boolean isStartToken() {
        return startToken;
    }
    
    public Token opposite() {
        return new Token(value, !startToken);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        if(!startToken) builder.append("/");
        builder.append(value);
        builder.append(">");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.startToken ? 1 : 0);
        hash = 31 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
    
    
    public boolean equals(Object o) {
        if(!(o instanceof Token)) {
            return false;
        }
        if(!((Token)o).value.equals(this.value)) return false;
        if(((Token)o).startToken != this.startToken) return false;
        
        return true;
    }
    
    public static Token anyCharacterToken() {
        return new Token("CHARACTER", true);
    }
}