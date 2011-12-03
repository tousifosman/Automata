package walker.statements;

import ast.StatementNode;
import walker.ExpressionDelegate;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.StatementExecutionException;

public interface StatementExecutor {
    public void execute(StatementNode node, ExpressionDelegate delegate) throws ASTExecutionException;
}
