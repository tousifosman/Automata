package walker.statements;

import ast.ExpressionNode;
import ast.Node;
import ast.StatementNode;
import java.io.PrintStream;
import java.util.List;
import walker.ExpressionDelegate;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.StatementExecutionException;

public class PrintStatement implements StatementExecutor {
    private PrintStream out;

    public PrintStatement(PrintStream out) {
        this.out = out;
    }

    @Override
    public void execute(StatementNode node, ExpressionDelegate delegate) throws ASTExecutionException {
        List<Node> subnodes = node.subnodes();

        for (Node subnode : subnodes) {
            if (!(subnode instanceof ExpressionNode)) {
                // Taylor TODO - Better exception
                throw new StatementExecutionException("Can't print non-expressions");
            }
            
            Object result = delegate.expand((ExpressionNode)subnode);
            out.println(result);
        }
    }

    public static String type() {
        return "print ( <exp-list> ) ;";
    }
}
