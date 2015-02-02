package skadistats.clarity.protogen.parser.model;

public class Package extends Node {

    private final StringLiteral name;

    public Package(StringLiteral name) {
        this.name = name;
    }
}
