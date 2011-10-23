package automata;

import java.util.Random;

public class State {
    private boolean finalState;
    private String name;
    private int id;
    
    private static int count = 0;
    
    public State() {
        this.name = "q" + count;
        this.id = (new Random()).nextInt();
        this.finalState = false;
        
        count++;
    }
   
    public boolean isFinal() {
        return finalState;
    }
    
    public void setFinal(boolean finalState) {
        this.finalState = finalState;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof State && ((State)o).id == this.id);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.id;
        return hash;
    }
}