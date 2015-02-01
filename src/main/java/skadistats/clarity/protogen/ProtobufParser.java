package skadistats.clarity.protogen;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;

@BuildParseTree
class ProtobufParser extends BaseParser<Object> {

    Rule Proto() {
        return Sequence(
            WS(),
            ZeroOrMore(FirstOf(Import(), Message(), Extend(), Enum(), Package(), Option(), ';'), WS()),
            WS()
        );
    }

    Rule Import() {
        return Sequence("import", WS(), StrLit(), WS(), ';');
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
        return Sequence(
            "enum", WSR(), Ident(), WS(), '{', WS(),
            ZeroOrMore(FirstOf(Option(), EnumField(), ';'), WS()),
            '}'
        );
    }

    Rule EnumField() {
        return Sequence(Ident(), WS(), '=', WS(), IntLit(), WS(), ';');
    }

    Rule Service() {
        return Sequence(
            "service", WSR(), Ident(), WS(), '{',
            ZeroOrMore(FirstOf(Option(), Rpc(), ';'), WS()),
            '}'
        );
    }

    Rule Rpc() {
        return Sequence("rpc", WSR(), Ident(), WS(), '(', WS(), UserType(), WS(), ')', WS(), "returns", WS(), '(', WS(), UserType(), WS(), ')', WS(), ';');
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
            "double", "float", "int32", "int64", "uint32", "uint64", "sint32", "sint64",
            "fixed32", "fixed64", "sfixed32", "sfixed64", "bool", "string", "bytes", UserType()
        );
    }

    @SuppressSubnodes
    Rule UserType() {
        return Sequence(Optional('.'), Ident(), ZeroOrMore('.', Ident()));
    }

    @SuppressSubnodes
    Rule Constant() {
        return FirstOf(
            Ident(), IntLit(), FloatLit(), StrLit(), BoolLit()
        );
    }

    @SuppressSubnodes
    Rule Ident() {
        return Sequence(
            FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), '_'),
            ZeroOrMore(FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), CharRange('0', '9'), '_'))
        );
    }

    @SuppressSubnodes
    Rule CamelIdent() {
        return Sequence(
            CharRange('A', 'Z'),
            ZeroOrMore(FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'), CharRange('0', '9'), '_'))
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
        return Sequence(Optional(AnyOf("+-")), OneOrMore(CharRange('0', '9')));
    }

    Rule HexInt() {
        return Sequence('0', AnyOf("xX"), OneOrMore(FirstOf(CharRange('A', 'F'), CharRange('a', 'f'), CharRange('0', '9'))));
    }

    Rule OctInt() {
        return Sequence('0', OneOrMore(CharRange('0', '7')));
    }

    @SuppressSubnodes
    Rule FloatLit() {
        return Sequence(
            OneOrMore(CharRange('0', '9')),
            Optional('.', OneOrMore(CharRange('0', '9'))),
            Optional(AnyOf("eE"), Optional(AnyOf("+-")), OneOrMore(CharRange('0', '9')))
        );
    }

    @SuppressSubnodes
    Rule BoolLit() {
        return FirstOf("true", "false");
    }

    @SuppressSubnodes
    Rule StrLit() {
        return Sequence(
            '"',
            ZeroOrMore(FirstOf(HexEscape(), OctEscape(), CharEscape(), Sequence(TestNot(AnyOf("\r\n\"\\")), ANY))).suppressSubnodes(),
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

}