package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class Field extends Node {
    private final StringLiteral modifier;
    private final Node type;
    private final Ident name;
    private final IntLiteral index;

    public Field(StringLiteral modifier, Node type, Ident name, IntLiteral index) {
        this.modifier = modifier;
        this.type = type;
        this.name = name;
        this.index = index;
    }

    @Override
    public String toString() {
        return "Field{" +
            "index=" + index +
            ", modifier=" + modifier +
            ", type=" + type +
            ", name=" + name +
            '}';
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write(modifier.getValue());
        w.write(' ');
        type.writeToProtoWriter(w);
        w.write(' ');
        name.writeToProtoWriter(w);
        w.write(" = ");
        index.writeToProtoWriter(w);
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
