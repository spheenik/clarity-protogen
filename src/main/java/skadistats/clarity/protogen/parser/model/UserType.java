package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserType extends Node {

    private final String prefix;
    private final List<Ident> pathNodes = new ArrayList<Ident>();

    public UserType(String prefix) {
        this.prefix = prefix;
    }

    public boolean addPathNode(Ident node) {
        pathNodes.add(node);
        return true;
    }

    @Override
    public String toString() {
        return "UserType{" +
            "pathNodes=" + pathNodes +
            ", prefix='" + prefix + '\'' +
            '}';
    }

    @Override
    public void outputProto(ProtoWriter w) {
        w.write(prefix);
        Iterator<Ident> iterator = pathNodes.iterator();
        while (iterator.hasNext()) {
            iterator.next().outputProto(w);
            if (iterator.hasNext()) {
                w.write(".");
            }
        }
    }

}
