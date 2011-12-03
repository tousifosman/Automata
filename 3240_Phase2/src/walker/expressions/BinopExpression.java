package walker.expressions;

import ast.ExpressionNode;
import ast.Node;
import java.util.ArrayList;
import java.util.List;
import walker.ExpressionDelegate;
import walker.datastructs.StringList;
import walker.datastructs.StringWithMetaData;
import walker.exceptions.ExpressionExpansionException;

public class BinopExpression implements ExpressionExpander {
    @Override
    public List expand(ExpressionNode node, ExpressionDelegate delegate) throws ExpressionExpansionException {
        List<Node> subnodes = node.subnodes();


        if (subnodes.isEmpty()
                || !(subnodes.get(0) instanceof ExpressionNode)) {
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
                && (subnodes.get(0) instanceof ExpressionNode)) {
            if (!(node.value() instanceof String)) {
                // Taylor TODO - Better exception
                throw new ExpressionExpansionException("Value must be binop type");
            }
            BinopType binopType = BinopType.toBinop((String) node.value());

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
