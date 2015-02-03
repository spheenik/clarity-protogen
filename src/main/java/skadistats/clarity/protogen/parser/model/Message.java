package skadistats.clarity.protogen.parser.model;

import org.eclipse.jgit.util.StringUtils;
import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

import java.util.LinkedList;

public class Message extends Node {

    private final Ident name;

    public Message(Ident name) {
        this.name = name;
    }

    public Ident getName() {
        return name;
    }

    public String getFullName() {
        LinkedList<String> values = new LinkedList<String>();
        for (Node n = this; n instanceof Message; n = n.getParent()) {
            values.addFirst(((Message) n).getName().getName());
        }
        values.addFirst("");
        return StringUtils.join(values, ".");
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        w.write("message ");
        name.writeToProtoWriter(w);
        w.write(" {");
        w.nextLine();
        w.increaseIndent();
        for (Node c : children) {
            c.writeToProtoWriter(w);
        }
        w.decreaseIndent();
        w.write('}');
        w.nextLine();
    }


}
