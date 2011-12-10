package walker.expressions;

import ast.ExpressionNode;
import ast.Node;
import java.util.List;
import walker.ExpressionDelegate;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.ExpressionExpansionException;
import walker.exceptions.IncorrectNodeTypeException;

public class ExpTail2Expression implements ExpressionExpander {
    @Override
    public Object expand(ExpressionNode node, ExpressionDelegate delegate, Object param) throws ASTExecutionException {
        throw new ExpressionExpansionException(this.getClass().getSimpleName() + " Error: should not execute");
    }

    public static String type() {
        return "EXP_TAIL2";
    }
}
