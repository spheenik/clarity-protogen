package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

public class Group extends Node {

    private final StringLiteral modifier;
    private final Ident ident;
    private final IntLiteral index;

    public Group(StringLiteral modifier, Ident ident, IntLiteral index) {
        this.modifier = modifier;
        this.ident = ident;
        this.index = index;
    }

    @Override
    public void outputProto(ProtoWriter w) {
        throw new RuntimeException("dunno how to render a " + this.getClass().getSimpleName());
    }

}
