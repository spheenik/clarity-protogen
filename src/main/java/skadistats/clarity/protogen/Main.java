package skadistats.clarity.protogen;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;
import skadistats.clarity.protogen.parser.ProtoWriter;
import skadistats.clarity.protogen.parser.ProtobufParser;
import skadistats.clarity.protogen.parser.model.ProtoTree;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.parboiled.common.Predicates.and;
import static org.parboiled.common.Predicates.not;
import static org.parboiled.support.Filters.rulesBelow;

public class Main {

    private static final File REPO_DIR = new File("./SteamKit/.git");
    private static final String PATH = "Resources/Protobufs/dota";
    private static final String REF = "refs/heads/master";

    private static final ProtobufParser PROTO_PARSER = Parboiled.createParser(ProtobufParser.class);

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
            //System.out.format("%s - %s\n", new SimpleDateFormat("dd.MM.yyyy HH:mm").format(d), commit.getShortMessage());

            TreeWalk treeWalk = new TreeWalk(REPO);
            treeWalk.addTree(commit.getTree());
            treeWalk.setRecursive(true);
            treeWalk.setFilter(PathFilter.create(PATH));
            while (treeWalk.next()) {
                //System.out.format("--- %s\n", treeWalk.getNameString());
                if (!treeWalk.getNameString().endsWith(".proto")) {
                    continue;
                }
                r.addChild(new ProtoFile(treeWalk.getNameString(), treeWalk.getObjectId(0)));
            }
            REVISIONS.add(r);
        }
        revWalk.dispose();
    }

    private static ProtoTree parseFile(String fileName) throws IOException {
        ReportingParseRunner<Node> runner = new ReportingParseRunner<Node>(PROTO_PARSER.Proto());
        ParsingResult<Node> result = runner.run(new String(Files.readAllBytes(Paths.get(fileName))));
        System.out.format("%s %s - %s\n", runner.getParseErrors().size(), runner.getValueStack().size(), fileName);

        ProtoTree p = (ProtoTree) runner.getValueStack().peek();

        p.writeToProtoWriter(new ProtoWriter(2, new PrintWriter(System.out)));

        for (ParseError error : runner.getParseErrors()) {
            System.out.println(error.getErrorMessage());
        }

        return (ProtoTree) runner.getValueStack().peek();
    }

    private static void dumpRevision(int n, Revision r) throws IOException {
        File dir = new File("./dump/" + n);
        dir.mkdir();
        System.out.println(dir.getPath());
        for (ProtoFile def : r.getChildren(ProtoFile.class)) {
            File out = new File(dir.getPath() + "/" + def.getFileName());
            def.writeToFile(REPO, out);
        }
    }

    private static void parseRevision(Revision r) throws IOException {
        for (ProtoFile def : r.getChildren(ProtoFile.class)) {
            ReportingParseRunner<Node> runner = new ReportingParseRunner<Node>(PROTO_PARSER.Proto());


            System.out.println("- RUNNING " + def.getFileName());
            ParsingResult<Node> result = runner.run(def.loadFromRepo(REPO));
            if (!result.matched) {
                TracingParseRunner<Node> traceRunner = new TracingParseRunner<Node>(PROTO_PARSER.Proto()).withFilter(
                    and(
                        not(rulesBelow(PROTO_PARSER.Ident())),
                        not(rulesBelow(PROTO_PARSER.StringLiteral())),
                        not(rulesBelow(PROTO_PARSER.FloatLiteral())),
                        not(rulesBelow(PROTO_PARSER.BoolLiteral())),
                        not(rulesBelow(PROTO_PARSER.WS())),
                        not(rulesBelow(PROTO_PARSER.WSR()))
                    )
                );
                ParsingResult<Node> traceResult = traceRunner.run(def.loadFromRepo(REPO));
                ParseTreeUtils.printNodeTree(traceResult);
                throw new IOException("errors wh¡ile parsing " + def.getFileName());
            } else {
                ProtoTree buf = (ProtoTree) result.valueStack.peek();
                buf.setProtoFile(def);
                def.setProtoTree(buf);
                buf.writeToProtoWriter(new ProtoWriter(2, new PrintWriter(System.out)));
            }
        }

    }

    private static void dumpAllRevisions() throws IOException {
        for (int i = REVISIONS.size() - 1; i >= 0 ; i--) {
            dumpRevision(REVISIONS.size() - i - 1, REVISIONS.get(i));
        }
    }

    public static void main(String[] args) throws Exception {
        readRevisions();

        Revision r = REVISIONS.get(0);
        parseRevision(r);
        for (ProtoFile def : r.getChildren(ProtoFile.class)) {
            System.out.println(def.getFileName() + " - " + def.getProtoTree().getAllMessages().size());
        }





        //dumpAllRevisions();
        //parseFile("./dump/316/demo.proto");
        //Protobuf buf = parseFile("./dump/316/dota_usermessages.proto");
        //System.out.println(buf.getAllMessages().size());


    }

}
