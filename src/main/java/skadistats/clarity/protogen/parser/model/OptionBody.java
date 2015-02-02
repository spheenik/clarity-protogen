package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

public class OptionBody extends Node {

    private final Ident id;
    private final Node value;

    public OptionBody(Ident id, Node value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public void outputProto(ProtoWriter w) {
        id.outputProto(w);
        w.write(" = ");
        value.outputProto(w);
    }

}
