/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package walker.statements;

import ast.Node;
import java.io.PrintStream;

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
    public void execute(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
