package skadistats.clarity.protogen.parser.model;

public class Import extends Node {

    private final StringLiteral importedFile;

    public Import(StringLiteral importedFile) {
        this.importedFile = importedFile;
    }

    @Override
    public String toString() {
        return "Import{" +
            "importedFile=" + importedFile +
            '}';
    }
}
