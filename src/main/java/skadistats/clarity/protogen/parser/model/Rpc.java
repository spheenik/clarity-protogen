package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class Rpc extends Node {

    private final Ident ident;
    private final UserType paramType;
    private final UserType returnType;

    public Rpc(Ident ident, UserType paramType, UserType returnType) {
        this.ident = ident;
        this.paramType = paramType;
        this.returnType = returnType;
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write("rpc ");
        ident.writeToProtoWriter(w);
        w.write(" (");
        paramType.writeToProtoWriter(w);
        w.write(") returns (");
        returnType.writeToProtoWriter(w);
        w.write(')');
        if (children.size() > 0) {
            w.write(" {");
            w.nextLine();
            w.increaseIndent();
            for (Node c : children) {
                c.writeToProtoWriter(w);
            }
            w.decreaseIndent();
            w.write('}');
        } else {
            w.write(';');
        }
        w.nextLine();
    }
}
