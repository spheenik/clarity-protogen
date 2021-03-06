package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class FloatLiteral extends Node {

    private final double value;

    public FloatLiteral(double value) {
        this.value = value;
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write(String.valueOf(value));
    }


}
