package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

public class Option extends Node {

    private final OptionBody body;

    public Option(OptionBody body) {
        this.body = body;
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write("option ");
        body.writeToProtoWriter(w);
        w.write(';');
        w.nextLine();
    }

}
