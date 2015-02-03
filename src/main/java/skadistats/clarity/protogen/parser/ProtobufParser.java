package skadistats.clarity.protogen.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import skadistats.clarity.protogen.Node;
import skadistats.clarity.protogen.parser.model.*;
import skadistats.clarity.protogen.parser.model.Package;

import java.math.BigDecimal;

@BuildParseTree
public class ProtobufParser extends BaseParser<Node> {

    public Rule Proto() {
        return Sequence(
            push(new ProtoTree()),
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
                            Option(),
                            Service()
                        ),
                        peek(1).addChild(pop())
                    ),
                    ';'
                ), WS()
            ),
            WS(),
            EOI
        );
    }

    public Rule Import() {
        return Sequence(
            "import", WS(), StringLiteral(), WS(), ';',
            push(new Import((StringLiteral) pop()))
        );
    }

    public Rule Package() {
        return Sequence(
            "package", WS(), Ident(), WS(), ';',
            push(new Package((StringLiteral) pop()))
        );
    }

    public Rule Option() {
        return Sequence(
            "option", WS(), OptionBody(), WS(), ';',
            push(new Option((OptionBody) pop()))
        );
    }

    public Rule OptionBody() {
        return Sequence(
            FirstOf(
                Ident(),
                Sequence('(', push(new CustomOptionIdent()), QualifiedIdent(), ')', peek(1).addChildren(pop().getChildren()))
            ),
            WS(), '=', WS(), Constant(),
            push(new OptionBody(pop(1), pop()))
        );
    }

    public Rule Message() {
        return Sequence("message", WSR(), Ident(), WS(), push(new Message((Ident) pop())), MessageBody());
    }

    public Rule Service() {
        return Sequence(
            "service", WSR(), Ident(), WS(), push(new Service((Ident) pop())), WS(),
            '{', WS(),
            ZeroOrMore(
                FirstOf(
                    Sequence(Option(), peek(1).addChild(pop())),
                    Sequence(Rpc(), peek(1).addChild(pop())),
                    ';'
                ), WS()
            ),
            '}'
        );
    }

    public Rule Rpc() {
        return Sequence(
            "rpc", WSR(), Ident(), WS(), '(', WS(), UserType(), WS(), ')', WS(), "returns", WS(), "(", WS(), UserType(), WS(), ')', WS(),
            push(new Rpc((Ident)pop(2), (UserType)pop(1), (UserType)pop())),
            FirstOf(
                Sequence(
                    '{',
                    ZeroOrMore(
                        WS(),
                        Sequence(Option(), peek(1).addChild(pop()))
                    ),
                    WS(),
                    '}'
                ),
                ';'
            )
        );
    }

    public Rule Extend() {
        return Sequence("extend", WS(), UserType(), WS(), push(new Extend((UserType)pop())), MessageBody());
    }

    public Rule Enum() {
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

    public Rule EnumField() {
        return Sequence(
            Ident(), WS(), '=', WS(), IntLiteral(), WS(),
            Optional(
                '[', WS(), OptionBody(), peek(1).addChild(pop()), WS(), ZeroOrMore(',', WS(), OptionBody(), peek(1).addChild(pop()), WS()), ']'
            ),
            ';',
            push(new EnumerationField((Ident) pop(1), (IntLiteral) pop()))
        );
    }

    public Rule MessageBody() {
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

    public Rule Group() {
        return Sequence(
            Modifier(), WSR(), "group", WSR(), CamelIdent(), WS(), '=', WS(), IntLiteral(), WS(),
            push(new Group((StringLiteral)pop(2), (Ident)pop(1), (IntLiteral)pop(0))),
            MessageBody()
        );
    }

    public Rule Field() {
        return Sequence(
            Modifier(), WSR(), Type(), WSR(), Ident(), WS(), '=', WS(), IntLiteral(), WS(),
            push(new Field((StringLiteral)pop(3), pop(2), (Ident)pop(1), (IntLiteral) pop(0))),
            Optional(
                '[', WS(), FieldOption(), peek(1).addChild(pop()), WS(), ZeroOrMore(',', WS(), FieldOption(), peek(1).addChild(pop()), WS()), ']'
            ),
            WS(), ';'
        );
    }

    public Rule FieldOption() {
        return FirstOf(
            Sequence("default", WS(), '=', WS(), Constant(), push(new Default(pop()))),
            OptionBody()
        );
    }

    public Rule Extensions() {
        return Sequence("extensions", IntLiteral(), "to", FirstOf(IntLiteral(), Sequence("max", push(new StringLiteral(match())))), push(new Extensions(pop(1), pop())), ';');
    }

    @SuppressSubnodes
    public Rule Modifier() {
        return Sequence(
            FirstOf("required", "optional", "repeated"),
            push(new StringLiteral(match()))
        );
    }

    @SuppressSubnodes
    public Rule Type() {
        return FirstOf(
            Sequence(
                FirstOf("double", "float", "int32", "int64", "uint32", "uint64", "sint32", "sint64", "fixed32", "fixed64", "sfixed32", "sfixed64", "bool", "string", "bytes"),
                push(new BuiltinType(match()))
            ),
            UserType()
        );
    }

    @SuppressSubnodes
    public Rule UserType() {
        return Sequence(
            Optional('.'),
            push(new UserType(match())),
            QualifiedIdent(),
            peek(1).addChildren(pop().getChildren())
        );
    }

    @SuppressSubnodes
    public Rule Constant() {
        return FirstOf(Ident(), IntLiteral(), FloatLiteral(), StringLiteral(), BoolLiteral());
    }

    @SuppressSubnodes
    public Rule QualifiedIdent() {
        return Sequence(
            push(new QualifiedIdent()),
            Ident(), peek(1).addChild(pop()),
            ZeroOrMore('.', Ident(), peek(1).addChild(pop()))
        );
    }

    @SuppressSubnodes
    public Rule Ident() {
        return Sequence(
            Sequence(
                FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), '_'),
                ZeroOrMore(FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), CharRange('0', '9'), '_'))
            ),
            push(new Ident(match()))
        );
    }

    @SuppressSubnodes
    public Rule CamelIdent() {
        return Sequence(
            Sequence(
                CharRange('A', 'Z'),
                ZeroOrMore(FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), CharRange('0', '9'), '_'))
            ),
            push(new Ident(match()))
        );
    }

    @SuppressSubnodes
    public Rule IntLiteral() {
        return Sequence(
            FirstOf(DecInt(), HexInt(), OctInt()),
            TestNot('.')
        );
    }

    public Rule DecInt() {
        return Sequence(
            Sequence(Optional(AnyOf("+-")), OneOrMore(CharRange('0', '9'))),
            push(new IntLiteral(IntLiteral.Type.DEC, new BigDecimal(match())))
        );
    }

    public Rule HexInt() {
        return Sequence(
            '0', AnyOf("xX"),
            Sequence(
                OneOrMore(FirstOf(CharRange('A', 'F'), CharRange('a', 'f'), CharRange('0', '9'))),
                push(new IntLiteral(IntLiteral.Type.HEX, BigDecimal.valueOf(Long.valueOf(match(), 16))))
            )
        );
    }

    public Rule OctInt() {
        return Sequence(
            '0',
            Sequence(
                OneOrMore(CharRange('0', '7')),
                push(new IntLiteral(IntLiteral.Type.OCT, BigDecimal.valueOf(Long.valueOf(match(), 8))))
            )
        );
    }

    @SuppressSubnodes
    public Rule FloatLiteral() {
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
    public Rule BoolLiteral() {
        return Sequence(FirstOf("true", "false"), push(new BoolLiteral(Boolean.valueOf(match()))));
    }

    @SuppressSubnodes
    public Rule StringLiteral() {
        return Sequence(
            '"',
            ZeroOrMore(FirstOf(HexEscape(), OctEscape(), CharEscape(), Sequence(TestNot(AnyOf("\r\n\"\\")), ANY))).suppressSubnodes(),
            push(new StringLiteral(match())),
            '"'
        );
    }

    public Rule HexEscape() {
        return Sequence(
            '\\', AnyOf("xX"),
            FirstOf(CharRange('A', 'F'), CharRange('a', 'f'), CharRange('0', '9')),
            Optional(FirstOf(CharRange('A', 'F'), CharRange('a', 'f'), CharRange('0', '9')))
        );
    }

    public Rule OctEscape() {
        return Sequence(
            '\\', Optional('0'),
            CharRange('0', '7'),
            Optional(CharRange('0', '7')),
            Optional(CharRange('0', '7'))
        );
    }

    public Rule CharEscape() {
        return Sequence('\\', AnyOf("abfnrtv\\?\""));
    }

    @SuppressNode
    public Rule WS() {
        return ZeroOrMore(AnyOf(" \t\f\r\n"));
    }

    @SuppressNode
    public Rule WSR() {
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