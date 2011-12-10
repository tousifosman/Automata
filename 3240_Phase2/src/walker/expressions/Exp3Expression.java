package walker.expressions;

import ast.ExpressionNode;
import ast.Node;
import java.util.List;
import walker.ExpressionDelegate;
import walker.datastructs.StringList;
import walker.datastructs.StringWithMetaData;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.ExpressionArgumentException;
import walker.exceptions.ExpressionExpansionException;
import walker.exceptions.IncorrectNodeTypeException;

public class Exp3Expression implements ExpressionExpander {
    @Override
    public List expand(ExpressionNode node, ExpressionDelegate delegate, Object param) throws ASTExecutionException {
        List<Node> subnodes = node.subnodes();


        if (subnodes.size() == 2 && subnodes.get(0) instanceof ExpressionNode && subnodes.get(1) instanceof ExpressionNode) {
            ExpressionNode exp_tail = (ExpressionNode) subnodes.get(0);
            ExpressionNode term = (ExpressionNode) subnodes.get(1);

            if (!term.type().equals(TermExpression.type())) {
                throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: Second node must be an Term");
            }

            if (exp_tail.type().equals(ExpTail1Expression.type())) {
                Object termVal = delegate.expand(term, null);
                return (List) delegate.expand(exp_tail, termVal);
            } else if (exp_tail.type().equals(ExpTail2Expression.type())) {
                return (List) delegate.expand(term, null);
            } else {
                throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: First node must be an ExpTail");
            }

        } else {
            throw new IncorrectNodeTypeException(this.getClass().getSimpleName() + " Error: Requires 2 ExpressionNodes (" + subnodes.size() + " given)", null);
        }
    }

    public static String type() {
        return "EXP3";
    }
}
