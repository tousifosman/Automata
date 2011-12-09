package walker.expressions;

import ast.ExpressionNode;
import ast.Node;
import java.util.List;
import walker.ExpressionDelegate;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.ExpressionExpansionException;
import walker.exceptions.IncorrectNodeTypeException;

public class SizeExpression implements ExpressionExpander {
    @Override
    public Integer expand(ExpressionNode node, ExpressionDelegate delegate) throws ASTExecutionException {
        List<Node> subnodes = node.subnodes();
        if (subnodes.size() != 1 || !(subnodes.get(0) instanceof ExpressionNode)) {
            throw new IncorrectNodeTypeException(this.getClass().getSimpleName() + " Error: Requires 1 ExpressionNode", subnodes.get(0));
        }

        Object subResult = delegate.expand((ExpressionNode) subnodes.get(0));

        if (!(subResult instanceof List)) {
            throw new ExpressionExpansionException(this.getClass().getSimpleName() + " Error: Can only take size of Lists (" + subResult.getClass() + " given)");
        }

        Integer result = ((List) subResult).size();

        return result;
    }

    public static String type() {
        return "# <exp>";
    }
}
