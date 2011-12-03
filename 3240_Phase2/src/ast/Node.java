package ast;

import java.util.List;

public abstract class Node {
    public abstract String type();
    
    public abstract String value();

    public abstract boolean hasNextNode();

    public abstract Node nextNode();

    public abstract List<Node> subnodes();
}
