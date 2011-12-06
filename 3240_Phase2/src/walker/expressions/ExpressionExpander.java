package walker.expressions;

import ast.ExpressionNode;
import walker.ExpressionDelegate;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.ExpressionExpansionException;

public interface ExpressionExpander {
    public Object expand(ExpressionNode node, ExpressionDelegate delegate) throws ASTExecutionException;
}
