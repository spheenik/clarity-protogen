package skadistats.clarity.protogen.parser.model;

public class Field extends Node {
    private final StringLiteral modifier;
    private final Node type;
    private final Ident name;
    private final IntLiteral index;

    public Field(StringLiteral modifier, Node type, Ident name, IntLiteral index) {
        this.modifier = modifier;
        this.type = type;
        this.name = name;
        this.index = index;
    }

    @Override
    public String toString() {
        return "Field{" +
            "index=" + index +
            ", modifier=" + modifier +
            ", type=" + type +
            ", name=" + name +
            '}';
    }
}
