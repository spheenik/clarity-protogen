package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class Enumeration extends Node {

    private final Ident name;

    public Enumeration(Ident name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Enumeration{" +
            "name=" + name +
            '}';
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write("enum ");
        name.writeToProtoWriter(w);
        w.write(" {");
        w.nextLine();
        w.increaseIndent();
        for (Node c : children) {
            c.writeToProtoWriter(w);
        }
        w.decreaseIndent();
        w.write('}');
        w.nextLine();
    }

}
