package skadistats.clarity.protogen;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

public class Revision extends Node {

    private final ObjectId id;

    public Revision(RevCommit commit) {
        this.id = commit.getId();
    }

    public ObjectId getId() {
        return id;
    }

}
