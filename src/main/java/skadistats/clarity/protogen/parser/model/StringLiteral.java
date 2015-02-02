package skadistats.clarity.protogen.parser.model;

public class StringLiteral extends Node {

    private final String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringLiteral{" +
            "value='" + value + '\'' +
            '}';
    }
}
