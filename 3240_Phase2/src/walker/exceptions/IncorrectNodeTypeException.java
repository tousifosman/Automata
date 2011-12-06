package walker.exceptions;

import ast.Node;

public class IncorrectNodeTypeException extends ASTExecutionException {
    
    public IncorrectNodeTypeException(String message, Node node) {
        super(message + " instead recieved " + node.getClass());
    }
    
}
