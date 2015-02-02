package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

public class Package extends Node {

    private final StringLiteral name;

    public Package(StringLiteral name) {
        this.name = name;
    }

    @Override
    public void outputProto(ProtoWriter w) {
        throw new RuntimeException("dunno how to render a " + this.getClass().getSimpleName());
    }

}
