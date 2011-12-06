package walker;

import ast.ExpressionNode;
import walker.exceptions.ASTExecutionException;

/**
 *
 * @author taylor
 */
public interface ExpressionDelegate {
    public Object expand(ExpressionNode node) throws ASTExecutionException;
}
