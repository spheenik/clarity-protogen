package skadistats.clarity.protogen.parser.model;

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
}
