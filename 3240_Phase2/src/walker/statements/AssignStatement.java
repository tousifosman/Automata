package walker.statements;

import ast.ExpressionNode;
import ast.Node;
import ast.StatementNode;
import java.util.List;
import java.util.Map;
import walker.ExpressionDelegate;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.ExpressionExpansionException;
import walker.exceptions.StatementExecutionException;

/**
 *
 * @author taylor
 */
public class AssignStatement implements StatementExecutor {
    protected Map<String, Object> idMap;

    public AssignStatement(Map<String, Object> idMap) {
        this.idMap = idMap;
    }

    @Override
    public void execute(StatementNode node, ExpressionDelegate delegate) throws ASTExecutionException {
        List<Node> subnodes = node.subnodes();
        if (!(node.value() instanceof String)) {
            // Taylor TODO - Better exception
            throw new StatementExecutionException("Value must be ID");
        }

        String id = (String) node.value();

        if (subnodes.size() != 1 || !(subnodes.get(0) instanceof ExpressionNode)) {
            // Taylor TODO - Better exception
            throw new StatementExecutionException("Not correct assignemnt");
        }

        Object result = delegate.expand((ExpressionNode) subnodes.get(0));

        idMap.put(id, result);
    }
}
