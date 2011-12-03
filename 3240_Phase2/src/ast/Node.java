package ast;

import java.util.LinkedList;
import java.util.List;

public abstract class Node {
    protected List<Node> subnodes;
    protected Node next;
    protected String type;
    protected Object value;

    public Node(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String type() {
        return type;
    }

    public Object value() {
        return value;
    }

    public boolean hasNextNode() {
        return (next != null);
    }

    public Node nextNode() {
        return next;
    }

    public List<Node> subnodes() {
        return subnodes;
    }

    public void setNextNode(Node next) {
        this.next = next;
    }

    public void addSubnode(Node child) {
        if (this.subnodes == null) {
            this.subnodes = new LinkedList<Node>();
        }
        this.subnodes.add(child);
    }

    public void setSubnodes(List<Node> subnodes) {
        this.subnodes = subnodes;
    }
}
