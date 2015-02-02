package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    private Node parent;
    protected List<Node> children = new ArrayList<Node>();

    public boolean addChild(Node child) {
        child.setParent(this);
        children.add(child);
        //System.out.format("added a %s to %s\n", child.getClass().getSimpleName(), this.getClass().getSimpleName());
        return true;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    abstract public void outputProto(ProtoWriter w);

}
