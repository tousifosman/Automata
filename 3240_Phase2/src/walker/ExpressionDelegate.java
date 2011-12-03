package walker;

import ast.ExpressionNode;
import ast.Node;
import walker.exceptions.ExpressionExpansionException;

/**
 *
 * @author taylor
 */
public interface ExpressionDelegate {
    public Object expand(ExpressionNode node) throws ExpressionExpansionException;
}
