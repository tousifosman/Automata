package walker.statements;

import ast.ExpressionNode;
import ast.Node;
import ast.StatementNode;
import java.util.List;
import walker.ExpressionDelegate;
import main.PrintStream;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.IncorrectNodeTypeException;
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
                throw new IncorrectNodeTypeException(this.getClass().getSimpleName() + " Error: Requires ExpressionNodes", subnode);
            }
            
            Object result = delegate.expand((ExpressionNode)subnode);
            out.println(result);
        }
    }

    public static String type() {
        return "print ( <exp-list> ) ;";
    }
}
