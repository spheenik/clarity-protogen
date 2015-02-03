package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.ProtoWriter;

import java.util.Iterator;

public class QualifiedIdent extends Node {

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        Iterator<Ident> iterator = getChildren(Ident.class).iterator();
        while (iterator.hasNext()) {
            iterator.next().writeToProtoWriter(w);
            if (iterator.hasNext()) {
                w.write(".");
            }
        }
    }

}
