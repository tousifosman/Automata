package ast;

public class AbstractSyntaxTree {
    private Node head;

    public AbstractSyntaxTree() {
        this(null);
    }

    public AbstractSyntaxTree(Node head) {
        this.head = head;
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }
}
