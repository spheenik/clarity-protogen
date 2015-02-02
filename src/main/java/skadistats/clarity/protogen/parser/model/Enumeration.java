package skadistats.clarity.protogen.parser.model;

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
    public void outputProto(ProtoWriter w) {
        w.write("enum ");
        name.outputProto(w);
        w.write(" {");
        w.nextLine();
        w.increaseIndent();
        for (Node c : children) {
            c.outputProto(w);
        }
        w.decreaseIndent();
        w.write('}');
        w.nextLine();
    }

}
