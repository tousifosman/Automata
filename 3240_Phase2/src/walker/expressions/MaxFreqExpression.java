package walker.expressions;

import ast.ExpressionNode;
import ast.Node;
import java.util.HashMap;
import java.util.List;
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
        if (!(node.value() instanceof String)) {
            // Taylor TODO - Better exception
            throw new ExpressionExpansionException("Value must be ID");
        }
        String id = (String)node.value();

        Object value = idMap.get(id);
        if (value == null) {
            // Taylor TODO - Better exception
            throw new ExpressionExpansionException("Undeclared ID");
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
            // Taylor TODO - Better exception
            throw new ExpressionExpansionException("ID should be a list");
        }
    }

    public static String type() {
        return "maxfreqstring (ID);";
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
