package skadistats.clarity.protogen.parser.model;

import skadistats.clarity.protogen.ProtobufDefinition;
import skadistats.clarity.protogen.parser.ProtoWriter;

import java.util.HashMap;
import java.util.Map;

public class Protobuf extends Node {

    private ProtobufDefinition definition;

    @Override
    public String toString() {
        return "Protobuf{}";
    }

    @Override
    public void outputProto(ProtoWriter w) {
        for (Node c : children) {
            c.outputProto(w);
        }
    }

    public Map<String, Message> getAllMessages() {
        HashMap<String, Message> result = new HashMap<String, Message>();
        getAllMessagesInternal(this, result);
        return result;
    }

    public void getAllMessagesInternal(Node node, Map<String, Message> messages) {
        for (Node child : node.children) {
            if (child instanceof Message) {
                Message m = (Message) child;
                messages.put(m.getFullName(), m);
                getAllMessagesInternal(m, messages);
            }
        }
    }

    public ProtobufDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(ProtobufDefinition definition) {
        this.definition = definition;
    }
}
