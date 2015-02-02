package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

import java.math.BigDecimal;

public class IntLiteral extends Node {

    public static enum Type {
        DEC, HEX, OCT
    }

    private final Type type;
    private final BigDecimal value;

    public IntLiteral(Type type, BigDecimal value) {
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

    @Override
    public void outputProto(ProtoWriter w) {
        w.write(value.toString());
    }

}
