package skadistats.clarity.protogen;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import skadistats.clarity.protogen.parser.model.ProtoTree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProtoFile extends Node {

    private final String fileName;
    private final ObjectId id;
    private ProtoTree protoTree;

    public ProtoFile(String fileName, ObjectId id) {
        this.fileName = fileName;
        this.id = id;
    }

    public String loadFromRepo(Repository repo) throws IOException {
        ObjectLoader loader = repo.open(id);
        return new String(loader.getBytes());
    }

    public void writeToFile(Repository repo, File out) throws IOException {
        ObjectLoader loader = repo.open(id);
        loader.copyTo(new FileOutputStream(out));
    }

    public String getFileName() {
        return fileName;
    }

    public ProtoTree getProtoTree() {
        return protoTree;
    }

    public void setProtoTree(ProtoTree protoTree) {
        this.protoTree = protoTree;
    }

}
