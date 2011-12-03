package walker.expressions;

import ast.ExpressionNode;
import java.util.Map;
import walker.ExpressionDelegate;
import walker.exceptions.ExpressionExpansionException;

public class MaxFreqExpression implements ExpressionExpander {
    private Map<String, Object> idMap;

    public MaxFreqExpression(Map<String, Object> idMap) {
        this.idMap = idMap;
    }

    @Override
    public Object expand(ExpressionNode node, ExpressionDelegate delegate) throws ExpressionExpansionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static String type() {
        return "maxfreqstring (ID);";
    }
}
