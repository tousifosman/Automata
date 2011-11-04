package automata;

import java.util.Random;
import java.util.Stack;

/**
 * Tokens are stored in the state in a stack. The top token is
 * the most general token applying to the State, and the bottom token is the 
 * most specific/smallest token.
 */
public class State {
    private boolean finalState;
    private String name;
    private int id;
    private Stack<Token> tokens;
    private static int count = 0;

    public State() {
        this(new Stack<Token>());
    }

    public State(Stack<Token> tokens) {
        this.tokens = new Stack<Token>();
        this.tokens.addAll(tokens);

        this.name = "q" + count;
        this.id = (new Random()).nextInt();
        this.finalState = false;

        count++;
    }

    public Stack<Token> getTokens() {
        Stack<Token> stack = new Stack<Token>();
        stack.addAll(this.tokens);
        return stack;
    }

    public void setTokens(Stack<Token> tokens) {
        this.tokens = tokens;
    }

    public Token topToken() {
        return tokens.peek();
    }

    public boolean isFinal() {
        return finalState;
    }

    public void setFinal(boolean finalState) {
        this.finalState = finalState;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof State && ((State) o).id == this.id);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.id;
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }

    public static void resetCount() {
        count = 0;
    }
}