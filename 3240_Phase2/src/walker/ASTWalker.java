package walker;

import main.PrintStream;
import java.io.File;
import walker.exceptions.InvalidStatementTypeException;
import walker.exceptions.ASTExecutionException;
import walker.exceptions.NonExecutableNodeException;
import ast.*;
import java.util.HashMap;
import java.util.Map;
import walker.exceptions.ExpressionExpansionException;
import walker.exceptions.InvalidExpressionTypeException;
import walker.exceptions.StatementExecutionException;
import walker.expressions.*;
import walker.statements.*;

public class ASTWalker implements ExpressionDelegate {
    private Map<String, StatementExecutor> executionMap;
    private Map<String, ExpressionExpander> expansionMap;
    private Map<String, Object> idMap;

    public ASTWalker(PrintStream out, File directory) {
        this.idMap = new HashMap<String, Object>();

        this.executionMap = new HashMap<String, StatementExecutor>();
        executionMap.put(AssignStatement.type(), new AssignStatement(idMap));
        executionMap.put(ReplaceStatement.type(), new ReplaceStatement(directory));
        executionMap.put(RecursiveReplaceStatement.type(), new RecursiveReplaceStatement(directory));
        executionMap.put(PrintStatement.type(), new PrintStatement(out));

        this.expansionMap = new HashMap<String, ExpressionExpander>();
        expansionMap.put(IdExpression.type(), new IdExpression(idMap));
        expansionMap.put(ParenExpression.type(), new ParenExpression());
        expansionMap.put(TermExpression.type(), new TermExpression(directory));
        expansionMap.put(BinopExpression.type(), new BinopExpression());
        expansionMap.put(SizeExpression.type(), new SizeExpression());
        expansionMap.put(MaxFreqExpression.type(), new MaxFreqExpression(idMap));
        expansionMap.put(IDStatement1Expression.type(), new IDStatement1Expression());
        expansionMap.put(Exp3Expression.type(), new Exp3Expression());
        expansionMap.put(ExpTail1Expression.type(), new ExpTail1Expression());
        
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

    private void execute(StatementNode node) throws ASTExecutionException {
        StatementExecutor executor = executionMap.get(node.type());

        if (executor == null) {
            throw new InvalidStatementTypeException(node);
        } else {
            executor.execute(node, this);
        }
    }

    @Override
    public Object expand(ExpressionNode node, Object params) throws ASTExecutionException {
        ExpressionExpander expander = expansionMap.get(node.type());
        
        if(expander == null) {
            throw new InvalidExpressionTypeException(node);
        }
        
        return expander.expand(node, this, params);
    }
}
