package walker.statements;

import ast.ExpressionNode;
import ast.Node;
import ast.StatementNode;
import java.util.List;
import walker.ExpressionDelegate;
import main.PrintStream;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.IncorrectNodeTypeException;
import walker.exceptions.StatementArgumentException;
import walker.exceptions.StatementExecutionException;

public class PrintStatement implements StatementExecutor {
    private PrintStream out;

    public PrintStatement(PrintStream out) {
        this.out = out;
    }

    @Override
    public void execute(StatementNode node, ExpressionDelegate delegate) throws ASTExecutionException {
        List<Node> subnodes = node.subnodes();
        if (subnodes.size() == 1 && subnodes.get(0) instanceof ExpressionNode && subnodes.get(0).type().equals("EXP_LIST")) {
            subnodes = subnodes.get(0).subnodes();
            if (subnodes.size() == 2 && subnodes.get(0) instanceof ExpressionNode && subnodes.get(1) instanceof ExpressionNode) {
                Object result = delegate.expand((ExpressionNode) subnodes.get(1), null);
                out.println(result);
                ExpressionNode currNode = (ExpressionNode) subnodes.get(0);

                while (currNode.type().equals("EXP_LIST_TAIL")) {
                    subnodes = currNode.subnodes();

                    if (subnodes.size() == 2 && subnodes.get(0) instanceof ExpressionNode && subnodes.get(1) instanceof ExpressionNode) {
                        result = delegate.expand((ExpressionNode) subnodes.get(1), null);
                        out.println(result);
                        currNode = (ExpressionNode) subnodes.get(0);
                    } else {
                        throw new IncorrectNodeTypeException(this.getClass().getSimpleName() + " Error: Requires 2 ExpressionNodes", null);
                    }

                }
                if (!currNode.type().equals("EXP_LIST_TAIL1")) {
                    throw new StatementArgumentException(this.getClass().getSimpleName() + " Error: Requires ExpListTails as left nodes");
                }
            } else {
                throw new IncorrectNodeTypeException(this.getClass().getSimpleName() + " Error: EXP_LIST Requires 2 ExpressionNodes", null);
            }
        } else {
            throw new StatementArgumentException(this.getClass().getSimpleName() + " Error: Requires 1 EXP_LIST, but got " + subnodes.get(0).type());

        }


    }

    public static String type() {
        return "print";
    }
}
