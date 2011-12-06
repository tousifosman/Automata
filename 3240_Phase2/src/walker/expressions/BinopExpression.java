package walker.expressions;

import ast.ExpressionNode;
import ast.Node;
import java.util.ArrayList;
import java.util.List;
import walker.ExpressionDelegate;
import walker.datastructs.StringList;
import walker.datastructs.StringWithMetaData;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.ExpressionArgumentException;
import walker.exceptions.ExpressionExpansionException;
import walker.exceptions.IncorrectNodeTypeException;

public class BinopExpression implements ExpressionExpander {
    @Override
    public List expand(ExpressionNode node, ExpressionDelegate delegate) throws ASTExecutionException {
        List<Node> subnodes = node.subnodes();


        if (subnodes.isEmpty()
                || !(subnodes.get(0) instanceof ExpressionNode)) {
            throw new IncorrectNodeTypeException(this.getClass() + " Error: Requires at least 1 ExpressionNode", subnodes.get(0));
        }

        if (subnodes.size() == 1) {
            Object result = delegate.expand((ExpressionNode) subnodes.get(0));

            if (!(result instanceof List)) {
                throw new ExpressionExpansionException(this.getClass() + " Error: Can only use Lists (" + result.getClass() + " given)");
            }

            return (List) result;
        } else if (subnodes.size() == 2
                && (subnodes.get(1) instanceof ExpressionNode)) {
            if (!(node.value() instanceof String)) {
                throw new ExpressionArgumentException(this.getClass() + " Error: Value must be String");
            }
            BinopType binopType = BinopType.toBinop((String) node.value());

            Object firstList = delegate.expand((ExpressionNode) subnodes.get(0));
            Object secondList = delegate.expand((ExpressionNode) subnodes.get(1));

            if (!(firstList instanceof List) || !(secondList instanceof List)) {
                throw new ExpressionExpansionException(this.getClass() + " Error: Can only use Lists (" + firstList.getClass() + " and " + secondList.getClass() + " given)");
            }

            return binopType.apply((List) firstList, (List) secondList);
        } else {
            throw new IncorrectNodeTypeException(this.getClass() + " Error: Requires 2 ExpressionNodes", subnodes.get(0));
        }
    }

    public static String type() {
        return "<bin-op>";
    }

    enum BinopType {
        INTERSECT,
        UNION,
        DIFFERENCE;

        public static BinopType toBinop(String str) throws ASTExecutionException {
            try {
                return valueOf(str.toUpperCase());
            } catch (Exception ex) {
                throw new ExpressionArgumentException("Binop Error: Invalid binop type (" + str + ")");
            }
        }

        private List apply(List<StringWithMetaData> firstList, List<StringWithMetaData> secondList) {
            List newList;
            switch (this) {
                case INTERSECT:
                    newList = new StringList();
                    newList.addAll(firstList);
                    newList.retainAll(secondList);
                    return newList;
                case UNION:
                    newList = new StringList();
                    newList.addAll(firstList);
                    newList.addAll(secondList);
                    return newList;
                case DIFFERENCE:
                    newList = new StringList();
                    newList.addAll(firstList);
                    newList.removeAll(secondList);
                    return newList;
                default:
                    return new StringList();
            }

        }
    }
}
