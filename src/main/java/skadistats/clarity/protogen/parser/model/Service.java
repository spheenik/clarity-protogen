package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class Service extends Node {

    private final Ident name;

    public Service(Ident name) {
        this.name = name;
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write("service ");
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
