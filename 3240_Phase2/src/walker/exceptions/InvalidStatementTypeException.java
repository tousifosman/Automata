
package walker.exceptions;

import ast.StatementNode;

public class InvalidStatementTypeException extends StatementExecutionException {
    public InvalidStatementTypeException(StatementNode node) {
        super("InvalidStatementTypeException " + node);
    }
}
