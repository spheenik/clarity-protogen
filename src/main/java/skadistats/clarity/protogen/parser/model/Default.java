package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class Default extends Node {

    private final Node value;

    public Default(Node value) {
        this.value = value;
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write("default = ");
        value.writeToProtoWriter(w);
    }

}
