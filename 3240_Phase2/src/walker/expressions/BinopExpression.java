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

public class BinopExpression implements ExpressionExpander {
    @Override
    public List expand(ExpressionNode node, ExpressionDelegate delegate, Object param) throws ASTExecutionException {

        if (!(node.value() instanceof String)) {
            throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: Requires a String as an argument");
        }

        if (param instanceof Object[] && ((Object[]) param).length == 2) {
            List list1 = (List)((Object[]) param)[0];
            List list2 = (List)((Object[]) param)[1];
            BinopType type = BinopType.toBinop((String) node.value());
            
            return type.apply(list1, list2);

        } else {
            throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: Requires 2 lists as parameters");

        }
    }

    public static String type() {
        return "BIN_OP";
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
