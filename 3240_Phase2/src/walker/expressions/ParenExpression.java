package walker.expressions;

import ast.ExpressionNode;
import ast.Node;
import java.util.List;
import walker.ExpressionDelegate;
import walker.exceptions.ExpressionExpansionException;

public class ParenExpression implements ExpressionExpander {
    @Override
    public Object expand(ExpressionNode node, ExpressionDelegate delegate) throws ExpressionExpansionException {
        List<Node> subnodes = node.subnodes();

        if (subnodes.size() != 1 || !(subnodes.get(0) instanceof ExpressionNode)) {
            // Taylor TODO  - better exception
            throw new ExpressionExpansionException("ParenExpression error");
        }

        return delegate.expand((ExpressionNode) subnodes.get(0));
    }

    public static String type() {
        return "( <exp> )";
    }
}
