package walker;

import walker.exceptions.InvalidStatementTypeException;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.NonExecutableNodeException;
import ast.*;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import walker.statements.*;

public class ASTWalker {
    private Map<String, StatementExecutor> executionMap;
    private Map<String, Object> idMap;

    public ASTWalker(PrintStream out) {
        this.idMap = new HashMap<String, Object>();
        this.executionMap = new HashMap<String, StatementExecutor>();

        executionMap.put("ID = <exp> ;", new Assign1Statement(idMap));
        executionMap.put("ID = # <exp> ; ", new Assign2Statement(idMap));
        executionMap.put("ID = maxfreqstring (ID);", new Assign3Statement(idMap));
        executionMap.put("replace REGEX with ASCII-STR in  <file-names> ;", new ReplaceStatement());
        executionMap.put("recursivereplace REGEX with ASCII-STR in  <file-names> ;", new RecursiveReplaceStatement());
        executionMap.put("print ( <exp-list> ) ;", new PrintStatement(out));
    }

    public void walk(AbstractSyntaxTree tree) throws ASTExecutionException {
        expandExecution(tree.getHead());
    }

    private void expandExecution(Node node) throws ASTExecutionException {
        if (node instanceof StatementNode) {
            execute((StatementNode) node);
        } else { //expression nodes can't be executed
            throw new NonExecutableNodeException(node);
        }
        if (node.hasNextNode()) {
            expandExecution(node.nextNode());
        }
    }

    private void execute(StatementNode node) throws InvalidStatementTypeException {
        StatementExecutor executor = executionMap.get(node.type());

        if (executor == null) {
            throw new InvalidStatementTypeException(node);
        } else {
            executor.execute(node);
        }
    }
}
