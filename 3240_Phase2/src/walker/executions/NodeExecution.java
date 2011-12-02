package walker.executions;

import ast.Node;
import walker.ASTWalker;

public interface NodeExecution {
    public void execute(Node node);
}
