package skadistats.clarity.protogen.parser.model;

import java.util.ArrayList;
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
}
