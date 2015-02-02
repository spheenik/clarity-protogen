package skadistats.clarity.protogen.parser.model;

import java.util.ArrayList;
import java.util.List;

public class Protobuf {

    private final List<Import> imports = new ArrayList<Import>();

    public Protobuf() {
    }

    public List<Import> getImports() {
        return imports;
    }

}
