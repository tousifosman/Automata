package walker.statements;

import ast.Node;
import walker.ASTWalker;

public interface StatementExecutor {
    public void execute(Node node);
}
