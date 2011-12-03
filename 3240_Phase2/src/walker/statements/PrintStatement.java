/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package walker.statements;

import ast.StatementNode;
import java.io.PrintStream;
import walker.ExpressionDelegate;

/**
 *
 * @author taylor
 */
public class PrintStatement implements StatementExecutor {
    private PrintStream out;
    
    public PrintStatement(PrintStream out) {
        this.out = out;
    }

    @Override
    public void execute(StatementNode node, ExpressionDelegate delegate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
