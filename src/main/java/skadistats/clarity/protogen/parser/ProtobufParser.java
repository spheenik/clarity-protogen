package skadistats.clarity.protogen.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import skadistats.clarity.protogen.parser.model.*;
import skadistats.clarity.protogen.parser.model.Package;

@BuildParseTree
public class ProtobufParser extends BaseParser<Node> {

    public Rule Proto() {
        return Sequence(
            push(new Protobuf()),
            WS(),
            ZeroOrMore(
                FirstOf(
                    Sequence(
                        FirstOf(
                            Import(),
                            Message(),
                            Extend(),
                            Enum(),
                            Package(),
                            Option()
                        ),
                        peek(1).addChild(pop())
                    ),
                    ';'
                ), WS()
            ),
            WS()
        );
    }

    Rule Import() {
        return Sequence(
            "import", WS(), StrLit(), WS(), ';',
            push(new Import((StringLiteral) pop()))
        );
    }

    Rule Package() {
        return Sequence(
            "package", WS(), Ident(), WS(), ';',
            push(new Package((StringLiteral) pop()))
        );
    }

    Rule Option() {
        return Sequence(
            "option", WS(), OptionBody(), WS(), ';',
            push(new Option((OptionBody) pop()))
        );
    }

    Rule OptionBody() {
        return Sequence(
            Ident(),
            ZeroOrMore(
                Sequence('.', Ident()),
                push(new Ident(((Ident) pop()).getName().concat(match())))
            ),
            WS(), '=', WS(), Constant(),
            push(new OptionBody((Ident) pop(1), pop()))
        );
    }

    Rule Message() {
        return Sequence("message", WS(), Ident(), WS(), push(new Message((Ident) pop())), MessageBody());
    }

    Rule Extend() {
        return Sequence("extend", WS(), UserType(), WS(), push(new Extend((UserType)pop())), MessageBody());
    }

    Rule Enum() {
        return Sequence(
            "enum", WSR(),
            Ident(), push(new Enumeration((Ident) pop())),
            WS(), '{', WS(),
            ZeroOrMore(
                FirstOf(
                    Sequence(Option(), peek(1).addChild(pop())),
                    Sequence(EnumField(), peek(1).addChild(pop())),
                    ';'
                ),
                WS()
            ),
            '}'
        );
    }

    Rule EnumField() {
        return Sequence(
            Ident(), WS(), '=', WS(), IntLit(), WS(), ';',
            push(new EnumerationField((Ident) pop(1), (IntLiteral) pop()))
        );
    }

    Rule MessageBody() {
        return Sequence(
            '{', WS(),
            ZeroOrMore(
                FirstOf(
                    Sequence(
                        FirstOf(Field(), Enum(), Message(), Extend(), Extensions(), Group(), Option()),
                        peek(1).addChild(pop())
                    ),
                    ':'
                ),
                WS()
            ),
            '}'
        );
    }

    Rule Group() {
        return Sequence(
            Modifier(), WSR(), "group", WSR(), CamelIdent(), WS(), '=', WS(), IntLit(), WS(),
            push(new Group((StringLiteral)pop(2), (Ident)pop(1), (IntLiteral)pop(0))),
            MessageBody()
        );
    }

    Rule Field() {
        return Sequence(
            Modifier(), WSR(), Type(), WSR(), Ident(), WS(), '=', WS(), IntLit(), WS(),
            push(new Field((StringLiteral)pop(3), pop(2), (Ident)pop(1), (IntLiteral) pop(0))),
            Optional(
                '[', WS(), FieldOption(), peek(1).addChild(pop()), WS(), ZeroOrMore(',', WS(), FieldOption(), peek(1).addChild(pop()), WS()), ']'
            ),
            WS(), ';'
        );
    }

    Rule FieldOption() {
        return FirstOf(
            Sequence("default", WS(), '=', WS(), Constant(), push(new Default(pop()))),
            OptionBody()
        );
    }

    Rule Extensions() {
        return Sequence("extensions", IntLit(), "to", FirstOf(IntLit(), Sequence("max", push(new StringLiteral(match())))), push(new Extensions(pop(1), pop())), ';');
    }

    @SuppressSubnodes
    Rule Modifier() {
        return Sequence(
            FirstOf("required", "optional", "repeated"),
            push(new StringLiteral(match()))
        );
    }

    @SuppressSubnodes
    Rule Type() {
        return FirstOf(
            Sequence(
                FirstOf("double", "float", "int32", "int64", "uint32", "uint64", "sint32", "sint64", "fixed32", "fixed64", "sfixed32", "sfixed64", "bool", "string", "bytes"),
                push(new BuiltinType(match()))
            ),
            UserType()
        );
    }

    @SuppressSubnodes
    Rule UserType() {
        return Sequence(
            Sequence(Optional('.'), push(new UserType(match()))),
            Ident(), ((UserType)peek(1)).addPathNode((Ident)pop()),
            ZeroOrMore('.', Ident(), ((UserType)peek(1)).addPathNode((Ident) pop()))
        );
    }

    @SuppressSubnodes
    Rule Constant() {
        return FirstOf(Ident(), IntLit(), FloatLit(), StrLit(), BoolLit());
    }

    @SuppressSubnodes
    Rule Ident() {
        return Sequence(
            Sequence(
                FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), '_'),
                ZeroOrMore(FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), CharRange('0', '9'), '_'))
            ),
            push(new Ident(match()))
        );
    }

    @SuppressSubnodes
    Rule CamelIdent() {
        return Sequence(
            Sequence(
                CharRange('A', 'Z'),
                ZeroOrMore(FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), CharRange('0', '9'), '_'))
            ),
            push(new Ident(match()))
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
            push(new IntLiteral(IntLiteral.Type.DEC, Integer.valueOf(match())))
        );
    }

    Rule HexInt() {
        return Sequence(
            Sequence('0', AnyOf("xX"), OneOrMore(FirstOf(CharRange('A', 'F'), CharRange('a', 'f'), CharRange('0', '9')))),
            push(new IntLiteral(IntLiteral.Type.HEX, 0))
        );
    }

    Rule OctInt() {
        return Sequence(
            Sequence('0', OneOrMore(CharRange('0', '7'))),
            push(new IntLiteral(IntLiteral.Type.OCT, 0))
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
            push(new FloatLiteral(Double.valueOf(match())))
        );
    }

    @SuppressSubnodes
    Rule BoolLit() {
        return Sequence(FirstOf("true", "false"), push(new BoolLiteral(Boolean.valueOf(match()))));
    }

    @SuppressSubnodes
    Rule StrLit() {
        return Sequence(
            '"',
            ZeroOrMore(FirstOf(HexEscape(), OctEscape(), CharEscape(), Sequence(TestNot(AnyOf("\r\n\"\\")), ANY))).suppressSubnodes(),
            push(new StringLiteral(match())),
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

    @Override
    public boolean push(Node value) {
        //System.out.println("push " + value);
        return super.push(value);
    }
}