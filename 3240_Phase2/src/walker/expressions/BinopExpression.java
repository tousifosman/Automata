
package walker.expressions;

import ast.ExpressionNode;
import ast.Node;
import java.util.ArrayList;
import java.util.List;
import walker.ExpressionDelegate;
import walker.exceptions.ExpressionExpansionException;

public class BinopExpression implements ExpressionExpander {
    @Override
    public List<String> expand(ExpressionNode node, ExpressionDelegate delegate) throws ExpressionExpansionException {
        List<Node> subnodes = node.subnodes();


        if (subnodes.isEmpty()
                || !(subnodes.get(0) instanceof ExpressionNode)
                || !subnodes.get(0).type().equals(TermExpression.type())) {
            // Taylor TODO - Better exception
            throw new ExpressionExpansionException("Invalid Binop subnodes");
        }

        if (subnodes.size() == 1) {
            Object result = delegate.expand((ExpressionNode) subnodes.get(0));

            if (!(result instanceof List)) {
                // Taylor TODO - Better exception
                throw new ExpressionExpansionException("Result should be list");
            }

            return (List) result;
        } else if (subnodes.size() == 2
                && (subnodes.get(0) instanceof ExpressionNode)
                && subnodes.get(0).type().equals(BinopExpression.type())) {

            BinopType binopType = BinopType.toBinop(node.value());

            Object firstList = delegate.expand((ExpressionNode) subnodes.get(0));
            Object secondList = delegate.expand((ExpressionNode) subnodes.get(1));

            if (!(firstList instanceof List) || !(secondList instanceof List)) {
                // Taylor TODO - Better exception
                throw new ExpressionExpansionException("Results should be list");
            }

            return binopType.apply((List) firstList, (List) secondList);
        } else {
            // Taylor TODO - Better exception
            throw new ExpressionExpansionException("Invalid Binop subnodes");
        }
    }

    public static String type() {
        return "<bin-op>";
    }

    enum BinopType {
        INTERSECT,
        UNION,
        DIFFERENCE;

        public static BinopType toBinop(String str) throws ExpressionExpansionException {
            try {
                return valueOf(str.toUpperCase());
            } catch (Exception ex) {
                // Taylor TODO - better exception
                throw new ExpressionExpansionException("invalid binop");
            }
        }

        private List apply(List firstList, List secondList) {
            List newList;
            switch (this) {
                case INTERSECT:
                    newList = new ArrayList(firstList.size() / 2);
                    for (Object o : secondList) {
                        if (firstList.contains(o)) newList.add(o);
                    }
                    return newList;
                case UNION:
                    newList = new ArrayList(firstList.size() + secondList.size() / 2);
                    newList.addAll(firstList);
                    for (Object o : secondList) {
                        if (!newList.contains(o)) newList.add(o);
                    }
                    return newList;
                case DIFFERENCE:
                    newList = new ArrayList(firstList.size() / 2);
                    for (Object o : firstList) {
                        if (!secondList.contains(o)) newList.add(o);
                    }
                    return newList;
                default:
                    return new ArrayList();
            }

        }
    }
}
