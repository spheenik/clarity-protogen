package skadistats.clarity.protogen.parser.model;

public class EnumerationField extends Node {

    private final Ident name;
    private final IntLiteral value;

    public EnumerationField(Ident name, IntLiteral value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "EnumerationField{" +
            "name=" + name +
            ", value=" + value +
            '}';
    }
}
