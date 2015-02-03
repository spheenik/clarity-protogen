package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class StringLiteral extends Node {

    private final String value;

    public StringLiteral(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StringLiteral{" +
            "value='" + value + '\'' +
            '}';
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write('"');
        w.write(value);
        w.write('"');
    }

}
