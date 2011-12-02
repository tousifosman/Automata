/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package walker.executions;

import ast.Node;
import java.io.PrintStream;

/**
 *
 * @author taylor
 */
public class PrintStatement implements NodeExecution {
    private PrintStream out;
    
    public PrintStatement(PrintStream out) {
        this.out = out;
    }

    @Override
    public void execute(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
