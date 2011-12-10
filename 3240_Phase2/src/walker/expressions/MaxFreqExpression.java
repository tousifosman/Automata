package walker.expressions;

import ast.ExpressionNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import walker.ExpressionDelegate;
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
        String id = (String)node.value();

        Object value = idMap.get(id);
        if (value == null) {
            throw new UninitializedIDException(this.getClass().getSimpleName() + "Error : Uninitialized ID (" + id + ")");
        }

        if (value instanceof List) {
            List list = (List) value;

            int maxSize = 0;
            Object maxObject = null;
            Counter counter = new Counter();

            for (Object o : list) {
                counter.increment(o);
                if (counter.count(o) > maxSize) {
                    maxSize = counter.count(o);
                    maxObject = o;
                }
            }

            return maxObject;
        } else {
            throw new ExpressionExpansionException(this.getClass().getSimpleName() + " Error: Can only use Lists (" + value.getClass() + " given)");
        }
    }

    public static String type() {
        return "maxfreqstring";
    }

    class Counter {
        private Map<Object, Integer> internalCount;

        public Counter() {
            internalCount = new HashMap<Object, Integer>();
        }

        public void increment(Object o) {
            if (!internalCount.containsKey(o)) {
                internalCount.put(o, 0);
            }

            internalCount.put(o, internalCount.get(o) + 1);
        }

        public int count(Object o) {
            if (!internalCount.containsKey(o)) {
                internalCount.put(o, 0);
            }

            return internalCount.get(o);
        }
    }
}
