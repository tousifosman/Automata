
package ast;

import java.util.List;

public abstract class Node {
    public abstract Object getType();
    public abstract Object getToken();
    public abstract Node getNextNode();
    public abstract List<Node> getSubNodes();
}
