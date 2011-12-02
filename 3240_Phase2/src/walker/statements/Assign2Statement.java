package walker.statements;

import ast.Node;
import java.util.Map;

public class Assign2Statement extends AssignStatement {
    public Assign2Statement(Map<String, Object> idMap) {
        super(idMap);
    }

    @Override
    public void execute(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
