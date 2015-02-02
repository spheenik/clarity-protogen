package skadistats.clarity.protogen.parser.model;

public class Enumeration extends Node {

    private final Ident name;

    public Enumeration(Ident name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Enumeration{" +
            "name=" + name +
            '}';
    }
}
