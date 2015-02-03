package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.ProtoFile;
import skadistats.clarity.protogen.parser.ProtoWriter;

import java.util.HashMap;
import java.util.Map;

public class ProtoTree extends Node {

    private ProtoFile protoFile;

    @Override
    public String toString() {
        return "Protobuf{}";
    }

    @Override
    public void writeToProtoWriter(ProtoWriter w) {
        for (Node c : children) {
            c.writeToProtoWriter(w);
        }
    }

    public Map<String, Message> getAllMessages() {
        HashMap<String, Message> result = new HashMap<String, Message>();
        getAllMessagesInternal(this, result);
        return result;
    }

    public void getAllMessagesInternal(Node node, Map<String, Message> messages) {
        for (Node child : node.getChildren()) {
            if (child instanceof Message) {
                Message m = (Message) child;
                messages.put(m.getFullName(), m);
                getAllMessagesInternal(m, messages);
            }
        }
    }

    public ProtoFile getProtoFile() {
        return protoFile;
    }

    public void setProtoFile(ProtoFile protoFile) {
        this.protoFile = protoFile;
    }
}
