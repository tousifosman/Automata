
package walker.exceptions;

import ast.StatementNode;

public class InvalidStatementTypeException extends ASTExecutionException {
    public InvalidStatementTypeException(StatementNode node) {
        super("InvalidStatementTypeException " + node);
    }
}
