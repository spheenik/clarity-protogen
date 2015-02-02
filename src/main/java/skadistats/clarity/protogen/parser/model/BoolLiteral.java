package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

public class BoolLiteral extends Node {
    private final boolean value;

    public BoolLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public void outputProto(ProtoWriter w) {
        throw new RuntimeException("dunno how to render a " + this.getClass().getSimpleName());
    }
}
