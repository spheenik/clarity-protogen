package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

public class Ident extends Node {

    private String name;

    public Ident(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Ident{" +
            "name='" + name + '\'' +
            '}';
    }

    @Override
    public void outputProto(ProtoWriter w) {
        w.write(name);
    }

}
