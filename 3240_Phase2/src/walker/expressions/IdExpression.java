package walker.expressions;

import ast.ExpressionNode;
import java.util.Map;
import walker.ExpressionDelegate;
import walker.exceptions.ExpressionExpansionException;


public class IdExpression implements ExpressionExpander {
    private Map<String, Object> idMap;
    
    public IdExpression(Map<String, Object> idMap) {
        this.idMap = idMap;
    }

    @Override
    public Object expand(ExpressionNode node, ExpressionDelegate delegate) throws ExpressionExpansionException {
        String id = node.value();
        
        Object value = idMap.get(id);
        if(value == null) {
            // Taylor TODO - Better exception
            throw new ExpressionExpansionException("Undeclared ID");
        }
        
        return value;
    }
    
}
