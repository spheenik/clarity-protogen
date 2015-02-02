package skadistats.clarity.protogen.parser.model;

import org.parboiled.trees.MutableTreeNodeImpl;

public class Import extends MutableTreeNodeImpl<Import> {

    private final String importedFile;

    public Import(String importedFile) {
        this.importedFile = importedFile;
    }
}
