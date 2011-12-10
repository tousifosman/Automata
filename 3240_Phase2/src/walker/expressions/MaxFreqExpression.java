package walker.expressions;

import ast.ExpressionNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import walker.ExpressionDelegate;
import walker.datastructs.StringWithMetaData;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.ExpressionArgumentException;
import walker.exceptions.ExpressionExpansionException;
import walker.exceptions.UninitializedIDException;

public class MaxFreqExpression implements ExpressionExpander {
    private Map<String, Object> idMap;

    public MaxFreqExpression(Map<String, Object> idMap) {
        this.idMap = idMap;
    }

    @Override
    public Object expand(ExpressionNode node, ExpressionDelegate delegate) throws ASTExecutionException {
        if (!(node.value() instanceof String)) {
            throw new ExpressionArgumentException(this.getClass().getSimpleName() + " Error: Value must be String");
        }
        String id = (String) node.value();

        Object value = idMap.get(id);
        if (value == null) {
            throw new UninitializedIDException(this.getClass().getSimpleName() + "Error : Uninitialized ID (" + id + ")");
        }

        if (value instanceof List) {
            List<StringWithMetaData> list = (List) value;

            int maxSize = 0;
            StringWithMetaData maxObject = null;

            for (StringWithMetaData o : list) {
                int size = o.size();
                if (size > maxSize) {
                    maxSize = size;
                    maxObject = o;
                }
            }

            return maxObject.getString();
        } else {
            throw new ExpressionExpansionException(this.getClass().getSimpleName() + " Error: Can only use Lists (" + value.getClass() + " given)");
        }
    }

    public static String type() {
        return "maxfreqstring";
    }
}
