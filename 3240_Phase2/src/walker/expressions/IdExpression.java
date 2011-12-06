package walker.expressions;

import ast.ExpressionNode;
import java.util.Map;
import walker.ExpressionDelegate;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.ExpressionArgumentException;
import walker.exceptions.UninitializedIDException;

public class IdExpression implements ExpressionExpander {
    private Map<String, Object> idMap;

    public IdExpression(Map<String, Object> idMap) {
        this.idMap = idMap;
    }

    @Override
    public Object expand(ExpressionNode node, ExpressionDelegate delegate) throws ASTExecutionException {
        if (!(node.value() instanceof String)) {
            throw new ExpressionArgumentException(this.getClass() + " Error: Value must be String");
        }
        String id = (String)node.value();

        Object value = idMap.get(id);
        if (value == null) {
            throw new UninitializedIDException(this.getClass() + "Error : Uninitialized ID (" + id + ")");
        }

        return value;
    }

    public static String type() {
        return "ID";
    }
}
