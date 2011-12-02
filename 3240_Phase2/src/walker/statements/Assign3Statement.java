package walker.statements;

import ast.Node;
import java.util.Map;

public class Assign3Statement extends AssignStatement {
    public Assign3Statement(Map<String, Object> idMap) {
        super(idMap);
    }

    @Override
    public void execute(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
