package walker;

import ast.Node;

public class NonExecutableNodeException extends ASTExecutionException {
    public NonExecutableNodeException(Node node) {
        super("Unable to execute Node " + node);
    }
}
