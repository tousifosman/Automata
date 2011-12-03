package walker.expressions;

import ast.ExpressionNode;
import ast.Node;
import java.util.List;
import walker.ExpressionDelegate;
import walker.exceptions.ExpressionExpansionException;

public class SizeExpression implements ExpressionExpander {
    @Override
    public Integer expand(ExpressionNode node, ExpressionDelegate delegate) throws ExpressionExpansionException {
        List<Node> subnodes = node.subnodes();
        if (subnodes.size() != 1 || !(subnodes.get(0) instanceof ExpressionNode)) {
            // Taylor TODO - better exception
            throw new ExpressionExpansionException("Invalid expression");
        }

        Object subResult = delegate.expand((ExpressionNode) subnodes.get(0));

        if (!(subResult instanceof List)) {
            // Taylor TODO - better exception
            throw new ExpressionExpansionException("Can only take size of lists");
        }

        Integer result = ((List) subResult).size();

        return result;
    }

    public static String type() {
        return "# <exp>";
    }
}
