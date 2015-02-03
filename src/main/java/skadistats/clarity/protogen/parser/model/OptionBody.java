package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class OptionBody extends Node {

    private final Node id;
    private final Node value;

    public OptionBody(Node id, Node value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        id.writeToProtoWriter(w);
        w.write(" = ");
        value.writeToProtoWriter(w);
    }

}
