package walker.exceptions;

import ast.ExpressionNode;

public class InvalidExpressionTypeException extends ExpressionExpansionException {
    public InvalidExpressionTypeException(ExpressionNode node) {
        super("InvalidExpressionTypeException " + node);
    }
}
