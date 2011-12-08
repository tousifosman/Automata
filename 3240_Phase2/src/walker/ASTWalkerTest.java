package walker;

import ast.*;
import walker.exceptions.ASTExecutionException;
import walker.expressions.*;
import walker.statements.*;

public class ASTWalkerTest {
    public static void main(String[] args) {

        StatementNode head = new StatementNode(AssignStatement.type(), "match_the");
        ExpressionNode binop = new ExpressionNode(BinopExpression.type(), null);
        ExpressionNode term = new ExpressionNode(TermExpression.type(), new String[]{"[A-Z a-z]*the[A-Z a-z]*", "input.txt"});
        binop.addSubnode(term);
        head.addSubnode(binop);

        StatementNode second = new StatementNode(AssignStatement.type(), "match_th");
        ExpressionNode binop2 = new ExpressionNode(BinopExpression.type(), null);
        ExpressionNode term2 = new ExpressionNode(TermExpression.type(), new String[]{"th[A-Z a-z]*", "input.txt"});
        binop2.addSubnode(term2);
        second.addSubnode(binop2);

        head.setNextNode(second);

        StatementNode third = new StatementNode(AssignStatement.type(), "count");
        ExpressionNode the_size = new ExpressionNode(SizeExpression.type(), null);
        ExpressionNode the_list = new ExpressionNode(IdExpression.type(), "match_the");
        third.addSubnode(the_size);
        the_size.addSubnode(the_list);

        second.setNextNode(third);

        StatementNode fourth = new StatementNode(PrintStatement.type(), null);
        ExpressionNode count = new ExpressionNode(IdExpression.type(), "count");
        fourth.addSubnode(count);

        third.setNextNode(fourth);

        StatementNode fifth = new StatementNode(AssignStatement.type(), "count");
        ExpressionNode th_size = new ExpressionNode(SizeExpression.type(), null);
        ExpressionNode th_list = new ExpressionNode(IdExpression.type(), "match_th");
        fifth.addSubnode(th_size);
        th_size.addSubnode(th_list);

        fourth.setNextNode(fifth);

        StatementNode sixth = new StatementNode(PrintStatement.type(), null);
        sixth.addSubnode(count);
        sixth.addSubnode(th_list);

        fifth.setNextNode(sixth);

        StatementNode seventh = new StatementNode(PrintStatement.type(), null);
        ExpressionNode binop3 = new ExpressionNode(BinopExpression.type(), "union");
        ExpressionNode term3 = new ExpressionNode(TermExpression.type(), new String[]{"[A-Z a-z]*z[A-Z a-z]*", "input.txt"});
        ExpressionNode binop4 = new ExpressionNode(BinopExpression.type(), null);
        ExpressionNode term4 = new ExpressionNode(TermExpression.type(), new String[]{"[A-Z a-z]*q[A-Z a-z]*", "input.txt"});
        seventh.addSubnode(binop3);
        binop3.addSubnode(term3);
        binop3.addSubnode(binop4);
        binop4.addSubnode(term4);

        sixth.setNextNode(seventh);

        StatementNode eighth = new StatementNode(ReplaceStatement.type(), new String[]{"we|We|they|They", "I", "input.txt", "result1.txt"});
        seventh.setNextNode(eighth);

        StatementNode ninth = new StatementNode(ReplaceStatement.type(), new String[]{"h[A-z a-z]*", "anana", "input.txt", "result2.txt"});
        eighth.setNextNode(ninth);

        AbstractSyntaxTree tree = new AbstractSyntaxTree(head);
        ASTWalker walker = new ASTWalker(System.out);
        try {
            walker.walk(tree);
        } catch (ASTExecutionException ex) {
            ex.printStackTrace();
        }
    }
}
