package skadistats.clarity.protogen.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.support.Var;
import skadistats.clarity.protogen.parser.model.Constant;
import skadistats.clarity.protogen.parser.model.Enumeration;
import skadistats.clarity.protogen.parser.model.Import;
import skadistats.clarity.protogen.parser.model.Protobuf;

@BuildParseTree
public class ProtobufParser extends BaseParser<Object> {

    public Rule Proto() {
        Var<Protobuf> p = new Var<Protobuf>(new Protobuf());
        return Sequence(
            WS(),
            ZeroOrMore(
                FirstOf(
                    Sequence(Import(), p.get().getImports().add((Import) pop())),
                    Message(),
                    Extend(),
                    Enum(),
                    Package(),
                    Option(),
                    ';'
                ), WS()
            ),
            WS()
        );
    }

    Rule Import() {
        return Sequence("import", WS(), StrLit(), WS(), ';', push(new Import((String) pop())));
    }

    Rule Package() {
        return Sequence("package", WS(), Ident(), WS(), ';');
    }

    Rule Option() {
        return Sequence("option", WS(), OptionBody(), WS(), ';');
    }

    Rule OptionBody() {
        return Sequence(Ident(), ZeroOrMore('.', Ident()), WS(), '=', WS(), Constant());
    }

    Rule Message() {
        return Sequence("message", WS(), Ident(), WS(), MessageBody());
    }

    Rule Extend() {
        return Sequence("extend", WS(), UserType(), WS(), MessageBody());
    }

    Rule Enum() {
        Var<Enumeration> e = new Var<Enumeration>();
        return Sequence(
            "enum", WSR(),
            Ident(), e.set(new Enumeration((String) pop())),
            WS(), '{', WS(),
            ZeroOrMore(
                FirstOf(
                    Sequence(Option(), dump("option: %s", pop())),
                    Sequence(EnumField(), e.get().addField((Enumeration.Field) pop())),
                    ';'
                ),
                WS()
            ),
            '}',
            push(e.get())
        );
    }

    Rule EnumField() {
        return Sequence(Ident(), WS(), '=', WS(), IntLit(), WS(), ';', push(new Enumeration.Field((String) pop(1), (Integer) pop())));
    }

    Rule MessageBody() {
        return Sequence(
            '{', WS(),
            ZeroOrMore(FirstOf(Field(), Enum(), Message(), Extend(), Extensions(), Group(), Option(), ':'), WS()),
            '}'
        );
    }

    Rule Group() {
        return Sequence(Modifier(), WSR(), "group", WSR(), CamelIdent(), WS(), '=', WS(), IntLit(), WS(), MessageBody());
    }

    Rule Field() {
        return Sequence(
            Modifier(), WSR(), Type(), WSR(), Ident(), WS(), '=', WS(), IntLit(), WS(),
            Optional(
                '[', WS(), FieldOption(), WS(), ZeroOrMore(',', WS(), FieldOption(), WS()), ']'
            ),
            WS(), ';'
        );
    }

    Rule FieldOption() {
        return FirstOf(
            OptionBody(),
            Sequence("default", WS(), '=', WS(), Constant())
        );
    }

    Rule Extensions() {
        return Sequence("extensions", IntLit(), "to", FirstOf(IntLit(), "max"), ';');
    }

    @SuppressSubnodes
    Rule Modifier() {
        return FirstOf("required", "optional", "repeated");
    }

    @SuppressSubnodes
    Rule Type() {
        return FirstOf(
            Sequence(
                FirstOf("double", "float", "int32", "int64", "uint32", "uint64", "sint32", "sint64", "fixed32", "fixed64", "sfixed32", "sfixed64", "bool", "string", "bytes"),
                dump("simple type %s", match())
            ),
            Sequence(
                UserType(),
                dump("user type %s", match())
            )
        );
    }

    @SuppressSubnodes
    Rule UserType() {
        return Sequence(Optional('.'), Ident(), ZeroOrMore('.', Ident()));
    }

    @SuppressSubnodes
    Rule Constant() {
        Var<Constant.Type> type = new Var<Constant.Type>();
        return
            Sequence(
                FirstOf(
                    Sequence(Ident(), type.set(Constant.Type.IDENT)),
                    Sequence(IntLit(), type.set(Constant.Type.INT)),
                    Sequence(FloatLit(), type.set(Constant.Type.FLOAT)),
                    Sequence(StrLit(), type.set(Constant.Type.STRING)),
                    Sequence(BoolLit(), type.set(Constant.Type.IDENT))
                ),
                push(new Constant(type.get(), pop()))
            );
    }

    @SuppressSubnodes
    Rule Ident() {
        return Sequence(
            Sequence(
                FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), '_'),
                ZeroOrMore(FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), CharRange('0', '9'), '_'))
            ),
            push(match())
        );
    }

    @SuppressSubnodes
    Rule CamelIdent() {
        return Sequence(
            Sequence(
                CharRange('A', 'Z'),
                ZeroOrMore(FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), CharRange('0', '9'), '_'))
            ),
            push(match())
        );
    }

    @SuppressSubnodes
    Rule IntLit() {
        return Sequence(
            FirstOf(DecInt(), HexInt(), OctInt()),
            TestNot('.')
        );
    }

    Rule DecInt() {
        return Sequence(
            Sequence(Optional(AnyOf("+-")), OneOrMore(CharRange('0', '9'))),
            push(Integer.valueOf(match()))
        );
    }

    Rule HexInt() {
        return Sequence(
            Sequence('0', AnyOf("xX"), OneOrMore(FirstOf(CharRange('A', 'F'), CharRange('a', 'f'), CharRange('0', '9')))),
            push(0) // TODO
        );
    }

    Rule OctInt() {
        return Sequence(
            Sequence('0', OneOrMore(CharRange('0', '7'))),
            push(0) // TODO
        );
    }

    @SuppressSubnodes
    Rule FloatLit() {
        return Sequence(
            Sequence(
                OneOrMore(CharRange('0', '9')),
                Optional('.', OneOrMore(CharRange('0', '9'))),
                Optional(AnyOf("eE"), Optional(AnyOf("+-")), OneOrMore(CharRange('0', '9')))
            ),
            push(Double.valueOf(match()))
        );
    }

    @SuppressSubnodes
    Rule BoolLit() {
        return Sequence(FirstOf("true", "false"), push(Boolean.valueOf(match())));
    }

    @SuppressSubnodes
    Rule StrLit() {
        return Sequence(
            '"',
            ZeroOrMore(FirstOf(HexEscape(), OctEscape(), CharEscape(), Sequence(TestNot(AnyOf("\r\n\"\\")), ANY))).suppressSubnodes(),
            push(match()),
            '"'
        );
    }

    Rule HexEscape() {
        return Sequence(
            '\\', AnyOf("xX"),
            FirstOf(CharRange('A', 'F'), CharRange('a', 'f'), CharRange('0', '9')),
            Optional(FirstOf(CharRange('A', 'F'), CharRange('a', 'f'), CharRange('0', '9')))
        );
    }

    Rule OctEscape() {
        return Sequence(
            '\\', Optional('0'),
            CharRange('0', '7'),
            Optional(CharRange('0', '7')),
            Optional(CharRange('0', '7'))
        );
    }

    Rule CharEscape() {
        return Sequence('\\', AnyOf("abfnrtv\\?\""));
    }

    @SuppressNode
    Rule WS() {
        return ZeroOrMore(AnyOf(" \t\f\r\n"));
    }

    @SuppressNode
    Rule WSR() {
        return OneOrMore(AnyOf(" \t\f\r\n"));
    }

    public boolean dump(String format, Object... parameters) {
        System.out.format(format + "\n", parameters);
        return true;
    }
}