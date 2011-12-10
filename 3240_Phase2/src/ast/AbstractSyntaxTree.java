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

    public String toString() {
        return toString(0, head);
    }

    private String toString(int level, Node node) {
        if (node == null) return "";

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < level; i++) ret.append("\t");

        ret.append(node.toString());
        ret.append("\n");

        if (node.subnodes() != null)
            for (Node subNode : node.subnodes())
                ret.append(toString(level + 1, subNode));


        ret.append(toString(level, node.nextNode()));

        return ret.toString();
    }
}
