package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

public class CustomOptionIdent extends QualifiedIdent {

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write('(');
        super.writeToProtoWriter(w);
        w.write(')');
    }
}
