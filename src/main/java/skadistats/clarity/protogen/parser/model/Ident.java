package skadistats.clarity.protogen.parser.model;

public class Ident extends Node {

    private String name;

    public Ident(String name) {
        this.name = name;
    }

    public boolean append(String suffix) {
        this.name = this.name.concat(suffix);
        return true;
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
}
