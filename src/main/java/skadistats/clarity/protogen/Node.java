package skadistats.clarity.protogen;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
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

    public boolean addChildren(List<Node> children) {
        for (Node child : children) {
            addChild(child);
        }
        return true;
    }

    public List<Node> getChildren() {
        return children;
    }

    public <T> Iterable<T> getChildren(final Class<T> clazz) {
        return (Iterable<T>) Iterables.filter(children, new Predicate<Node>() {
            @Override
            public boolean apply(Node node) {
                return clazz.isAssignableFrom(node.getClass());
            }
        });
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void writeToProtoWriter(ProtoWriter w) {
        throw new RuntimeException("dunno how to render a " + this.getClass().getSimpleName());
    }

}
