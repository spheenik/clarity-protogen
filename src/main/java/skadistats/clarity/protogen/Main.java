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
import org.parboiled.support.ParsingResult;
import skadistats.clarity.protogen.parser.ProtoWriter;
import skadistats.clarity.protogen.parser.ProtobufParser;
import skadistats.clarity.protogen.parser.model.Node;
import skadistats.clarity.protogen.parser.model.Protobuf;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
                r.addProto(new ProtobufDefinition(treeWalk.getNameString(), treeWalk.getObjectId(0)));
            }
            REVISIONS.add(r);
        }
        revWalk.dispose();
    }

    private static Protobuf parseFile(String fileName) throws IOException {
//        TracingParseRunner<Object> runner = new TracingParseRunner<Object>(p.Proto()).withFilter(
//            and(
//                not(rulesBelow(p.Ident())),
//                not(rulesBelow(p.StrLit())),
//                not(rulesBelow(p.FloatLit())),
//                not(rulesBelow(p.BoolLit())),
//                not(rulesBelow(p.WS())),
//                not(rulesBelow(p.WSR()))
//            )
//        );

        ReportingParseRunner<Node> runner = new ReportingParseRunner<Node>(PROTO_PARSER.Proto());
        ParsingResult<Node> result = runner.run(new String(Files.readAllBytes(Paths.get(fileName))));
        System.out.format("%s %s - %s\n", runner.getParseErrors().size(), runner.getValueStack().size(), fileName);

        Protobuf p = (Protobuf) runner.getValueStack().peek();

        p.outputProto(new ProtoWriter(2, new PrintWriter(System.out)));

        for (ParseError error : runner.getParseErrors()) {
            System.out.println(error.getErrorMessage());
        }

        return (Protobuf) runner.getValueStack().peek();
        //System.out.println("done, here are the nodes:");
        //System.out.println(ParseTreeUtils.printNodeTree(result));
    }



    private static void dumpRevision(int n, Revision r) throws IOException {
        File dir = new File("./dump/" + n);
        dir.mkdir();
        System.out.println(dir.getPath());
        for (ProtobufDefinition def : r.getProtobufDefinitionList()) {
            File out = new File(dir.getPath() + "/" + def.getName());
            def.writeToFile(REPO, out);
        }
    }


    private static void parseRevision(Revision r) throws IOException {
        for (ProtobufDefinition def : r.getProtobufDefinitionList()) {
            ReportingParseRunner<Node> runner = new ReportingParseRunner<Node>(PROTO_PARSER.Proto());
            ParsingResult<Node> result = runner.run(def.loadFromRepo(REPO));
            if (!result.matched) {
                for (ParseError error : runner.getParseErrors()) {
                    System.out.println(error.getErrorMessage());
                }
                throw new IOException("errors while parsing protobuf-files.");
            }
            Protobuf buf = (Protobuf) result.valueStack.peek();
            buf.setDefinition(def);
            def.setProto(buf);
            buf.outputProto(new ProtoWriter(2, new PrintWriter(System.out)));
        }

    }


    private static void dumpAllRevisions() throws IOException {
        for (int i = REVISIONS.size() - 1; i >= 0 ; i--) {
            dumpRevision(REVISIONS.size() - i - 1, REVISIONS.get(i));
        }
    }

    public static void main(String[] args) throws Exception {
        readRevisions();

        //dumpAllRevisions();
        //parseFile("./dump/316/demo.proto");
        //Protobuf buf = parseFile("./dump/316/dota_usermessages.proto");
        //System.out.println(buf.getAllMessages().size());

        parseRevision(REVISIONS.get(0));

    }

}
