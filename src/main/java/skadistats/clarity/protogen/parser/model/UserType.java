package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

public class UserType extends QualifiedIdent {

    private final String prefix;

    public UserType(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write(prefix);
        super.writeToProtoWriter(w);
    }

}
