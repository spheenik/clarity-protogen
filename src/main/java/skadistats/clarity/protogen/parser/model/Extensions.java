package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

public class Extensions extends Node {
    private final Node from;
    private final Node to;

    public Extensions(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void outputProto(ProtoWriter w) {
        throw new RuntimeException("dunno how to render a " + this.getClass().getSimpleName());
    }

}
