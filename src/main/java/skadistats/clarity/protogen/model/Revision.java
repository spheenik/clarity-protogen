package skadistats.clarity.protogen.model;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.List;

public class Revision {

    private final ObjectId id;
    private final List<ProtobufDefinition> protobufDefinitionList = new ArrayList<ProtobufDefinition>();

    public Revision(RevCommit commit) {
        this.id = commit.getId();
    }

    public void addProto(ProtobufDefinition protobufDefinition) {
        protobufDefinitionList.add(protobufDefinition);
    }

}
