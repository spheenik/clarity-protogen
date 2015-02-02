package skadistats.clarity.protogen.parser.model;

public class Extensions extends Node {
    private final Node from;
    private final Node to;

    public Extensions(Node from, Node to) {
        this.from = from;
        this.to = to;
    }
}
