package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class BoolLiteral extends Node {
    private final boolean value;

    public BoolLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write(String.valueOf(value));
    }
}
