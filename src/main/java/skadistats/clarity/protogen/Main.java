package skadistats.clarity.protogen;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import skadistats.clarity.protogen.model.ProtobufDefinition;
import skadistats.clarity.protogen.model.Revision;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Main {

    private static final File REPO_DIR = new File("./SteamKit/.git");
    private static final String PATH = "Resources/Protobufs/dota";
    private static final String REF = "refs/heads/master";

    private static final List<Revision> REVISIONS = new ArrayList<Revision>();

    private static final Repository REPO;
    static {
        try {
            if (!REPO_DIR.isDirectory()) {
                throw new IOException("SteamKit-Repo not found!");
            }
            REPO = new FileRepositoryBuilder().setGitDir(REPO_DIR).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readRevisions() throws IOException {
        Ref headRef = REPO.getRef(REF);

        RevWalk revWalk = new RevWalk(REPO);
        revWalk.markStart(revWalk.lookupCommit(headRef.getObjectId()));
        revWalk.setTreeFilter(PathFilter.create(PATH));
        Iterator<RevCommit> iterator = revWalk.iterator();
        while (iterator.hasNext()) {
            RevCommit commit = iterator.next();
            Revision r = new Revision(commit);

            Date d = new Date();
            d.setTime(commit.getCommitTime() * 1000L);
            System.out.format("%s - %s\n", new SimpleDateFormat("dd.MM.yyyy HH:mm").format(d), commit.getShortMessage());

            TreeWalk treeWalk = new TreeWalk(REPO);
            treeWalk.addTree(commit.getTree());
            treeWalk.setRecursive(true);
            treeWalk.setFilter(PathFilter.create(PATH));
            while (treeWalk.next()) {
                System.out.format("--- %s\n", treeWalk.getNameString());
                r.addProto(new ProtobufDefinition(treeWalk.getNameString(), treeWalk.getObjectId(0)));
            }
            REVISIONS.add(r);
        }
        revWalk.dispose();
    }

    public static void main(String[] args) throws IOException {
        readRevisions();
    }
}
