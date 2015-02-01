package skadistats.clarity.protogen.model;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProtobufDefinition {

    private final String name;
    private final ObjectId id;

    public ProtobufDefinition(String name, ObjectId id) {
        this.name = name;
        this.id = id;
    }

    public String loadFromRepo(Repository repo) throws IOException {
        ObjectLoader loader = repo.open(id);
        return new String(loader.getBytes());
    }


    public String getName() {
        return name;
    }

    public void writeToFile(Repository repo, File out) throws IOException {
        ObjectLoader loader = repo.open(id);
        loader.copyTo(new FileOutputStream(out));
    }
}
