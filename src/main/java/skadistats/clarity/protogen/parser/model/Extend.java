package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

public class Extend extends Node {

    private final UserType type;

    public Extend(UserType type) {
        this.type = type;
    }

    @Override
    public void outputProto(ProtoWriter w) {
        w.write("extend ");
        type.outputProto(w);
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
