package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;

public class Package extends Node {

    private final StringLiteral name;

    public Package(StringLiteral name) {
        this.name = name;
    }

}
