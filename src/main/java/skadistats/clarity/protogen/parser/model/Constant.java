package skadistats.clarity.protogen.parser.model;

public class Constant {

    public static enum Type {
        IDENT, INT, FLOAT, STRING, BOOLEAN
    }

    private final Type type;
    private final Object value;

    public Constant(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

}
