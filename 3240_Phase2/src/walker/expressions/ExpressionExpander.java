package walker.expressions;

import ast.Node;

public interface ExpressionExpander {
    public Object expand(Node node);
}
