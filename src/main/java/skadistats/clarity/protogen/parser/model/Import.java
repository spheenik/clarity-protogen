package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.parser.ProtoWriter;

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

    @Override
    public void outputProto(ProtoWriter w) {
        w.write("import ");
        importedFile.outputProto(w);
        w.write(';');
        w.nextLine();
    }
}
