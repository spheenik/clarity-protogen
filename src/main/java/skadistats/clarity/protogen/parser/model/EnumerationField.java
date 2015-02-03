package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class EnumerationField extends Node {

    private final Ident name;
    private final IntLiteral value;

    public EnumerationField(Ident name, IntLiteral value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "EnumerationField{" +
            "name=" + name +
            ", value=" + value +
            '}';
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        name.writeToProtoWriter(w);
        w.write(" = ");
        value.writeToProtoWriter(w);
        if (children.size() > 0) {
            w.write(" [");
            for (Node c : children) {
                c.writeToProtoWriter(w);
            }
            w.write(']');
        }
        w.write(';');
        w.nextLine();
    }

}
