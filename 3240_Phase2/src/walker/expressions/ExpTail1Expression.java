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

public class ExpTail1Expression implements ExpressionExpander {
    @Override
    public List expand(ExpressionNode node, ExpressionDelegate delegate, Object param) throws ASTExecutionException {
        List<Node> subnodes = node.subnodes();
        if (param instanceof List) {
            if (subnodes.size() == 3 && subnodes.get(0) instanceof ExpressionNode && subnodes.get(1) instanceof ExpressionNode && subnodes.get(2) instanceof ExpressionNode) {
                ExpressionNode exp_tail = (ExpressionNode) subnodes.get(0);
                ExpressionNode term = (ExpressionNode) subnodes.get(1);
                ExpressionNode binop = (ExpressionNode) subnodes.get(2);

                if (!term.type().equals(TermExpression.type())) {
                    throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: Second node must be an Term");
                }
                if (!binop.type().equals(BinopExpression.type())) {
                    throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: Second node must be an Binop");
                }

                if (exp_tail.type().equals(ExpTail1Expression.type())) {

                    Object termVal = delegate.expand(term, null);
                    Object[] params = {param, termVal};
                    Object nextParam = delegate.expand(binop, params);

                    return (List) delegate.expand(exp_tail, nextParam);
                } else if (exp_tail.type().equals(ExpTail2Expression.type())) {
                    Object termVal = delegate.expand(term, null);
                    Object[] params = {param, termVal};
                    return (List) delegate.expand(binop, params);
                } else {
                    throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: First node must be an ExpTail");
                }

            } else {
                throw new IncorrectNodeTypeException(this.getClass().getSimpleName() + " Error: Requires 3 ExpressionNodes (" + subnodes.size() + " given)", null);
            }
        } else {
            throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: Requires a List as a parameter");
        }
    }

    public static String type() {
        return "EXP_TAIL1";
    }
}
