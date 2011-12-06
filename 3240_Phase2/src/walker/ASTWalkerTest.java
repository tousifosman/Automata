package walker;

import ast.*;
import walker.exceptions.ASTExecutionException;
import walker.expressions.*;
import walker.statements.*;

public class ASTWalkerTest {
    public static void main(String[] args) {
        StatementNode head = new StatementNode(AssignStatement.type(), "a");
        ExpressionNode binop = new ExpressionNode(BinopExpression.type(), "intersect");
        ExpressionNode term = new ExpressionNode(TermExpression.type(), new String[]{"[A-Z a-z]*ment[A-Z a-z]*", "file1.txt"});

        ExpressionNode binop2 = new ExpressionNode(BinopExpression.type(), null);
        ExpressionNode term2 = new ExpressionNode(TermExpression.type(), new String[]{"(A|a) [A-Z a-z]*", "file2.txt"});

        binop.addSubnode(term);
        binop.addSubnode(binop2);
        binop2.addSubnode(term2);

        head.addSubnode(binop);


        StatementNode second = new StatementNode(AssignStatement.type(), "b");
        ExpressionNode binop3 = new ExpressionNode(BinopExpression.type(), null);
        ExpressionNode term3 = new ExpressionNode(TermExpression.type(), new String[]{"[A-Z a-z]*ment[A-Z a-z]*", "file1.txt"});
        binop3.addSubnode(term3);

        second.addSubnode(term3);
        head.setNextNode(second);

        StatementNode third = new StatementNode(AssignStatement.type(), "c");
        ExpressionNode binop4 = new ExpressionNode(BinopExpression.type(), null);
        ExpressionNode term4 = new ExpressionNode(TermExpression.type(), new String[]{"(A|a) [A-Z a-z]*", "file2.txt"});
        binop4.addSubnode(term4);

        third.addSubnode(term4);
        second.setNextNode(third);
        
        
        StatementNode print = new StatementNode(PrintStatement.type(), null);
        ExpressionNode a = new ExpressionNode(IdExpression.type(), "a");
        ExpressionNode b = new ExpressionNode(IdExpression.type(), "b");
        ExpressionNode c = new ExpressionNode(IdExpression.type(), "c");
        ExpressionNode d = new ExpressionNode(SizeExpression.type(), null);
        d.addSubnode(a);
        
        print.addSubnode(a);
        print.addSubnode(b);
        print.addSubnode(c);
        print.addSubnode(d);
        
        third.setNextNode(print);
        
        StatementNode replace = new StatementNode(ReplaceStatement.type(), new String[]{"[A-Z a-z]*ment", "", "file1.txt", "file3.txt"});
        print.setNextNode(replace);

        AbstractSyntaxTree tree = new AbstractSyntaxTree(head);
        ASTWalker walker = new ASTWalker(System.out);
        try {
            walker.walk(tree);
        } catch (ASTExecutionException ex) {
            System.out.println("error");
        }
    }
}
