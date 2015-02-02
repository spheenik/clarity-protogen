package skadistats.clarity.protogen.parser.model;

public class OptionBody extends Node {

    private final Ident node;
    private final Node value;

    public OptionBody(Ident node, Node value) {
        this.node = node;
        this.value = value;
    }
}
