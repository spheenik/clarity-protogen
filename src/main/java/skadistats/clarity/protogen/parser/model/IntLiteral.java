package skadistats.clarity.protogen.parser.model;

public class IntLiteral extends Node {

    public static enum Type {
        DEC, HEX, OCT
    }

    private final Type type;
    private final int value;

    public IntLiteral(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "IntLiteral{" +
            "type=" + type +
            ", value=" + value +
            '}';
    }
}
