package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

public class BuiltinType extends Node {

    private final String type;

    public BuiltinType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BuiltinType{" +
            "type='" + type + '\'' +
            '}';
    }

    @Override
    public void outputProto(ProtoWriter w) {
        w.write(type);
    }

}
