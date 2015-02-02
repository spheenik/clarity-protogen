package skadistats.clarity.protogen.parser.model;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private Node parent;
    private List<Node> children = new ArrayList<Node>();

    public boolean addChild(Node child) {
        child.setParent(this);
        children.add(child);
        System.out.format("added a %s to %s\n", child.getClass().getSimpleName(), this.getClass().getSimpleName());
        return true;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

}
