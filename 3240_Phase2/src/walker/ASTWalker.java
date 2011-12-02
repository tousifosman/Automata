package walker;

import ast.*;
import java.io.PrintStream;

public class ASTWalker {
    private AbstractSyntaxTree tree;

    public ASTWalker(AbstractSyntaxTree tree) {
        this.tree = tree;
    }
    private PrintStream out;

    public void walk(PrintStream out) throws ASTExecutionException {
        this.out = out;
        expandExecution(tree.getHead());
    }

    private void expandExecution(Node node) throws NonExecutableNodeException {
        if (node instanceof StatementNode) {
            execute((StatementNode)node);
        } else { //expression nodes can't be executed
            throw new NonExecutableNodeException(node);
        }
        if (node.hasNextNode()) {
            expandExecution(node.nextNode());
        }
    }

    private void execute(StatementNode node) {
        
    } 
}
