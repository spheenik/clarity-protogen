package skadistats.clarity.protogen.parser;

import java.io.PrintWriter;

public class ProtoWriter {

    private int indentPerLevel;
    private int indentLevel = 0;
    private boolean lineStart = true;

    private final PrintWriter writer;

    public ProtoWriter(int indentPerLevel, PrintWriter writer) {
        this.indentPerLevel = indentPerLevel;
        this.writer = writer;
    }

    private void ensureIndent() {
        if (lineStart) {
            for (int i = 0; i < indentLevel * indentPerLevel; i++) {
                writer.write(" ");
            }
            lineStart = false;
        }
    }

    public void nextLine() {
        writer.write("\n");
        lineStart = true;
        writer.flush();
    }

    public void write(char c) {
        ensureIndent();
        writer.write(c);
        writer.flush();
    }

    public void write(String s) {
        ensureIndent();
        writer.write(s);
        writer.flush();
    }

    public void format(String format, Object... args) {
        ensureIndent();
        writer.format(format, args);
        writer.flush();
    }

    public void increaseIndent() {
        indentLevel++;
    }

    public void decreaseIndent() {
        indentLevel--;
    }

}
