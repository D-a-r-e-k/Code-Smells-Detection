public static Object case410_line1573(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    int options = ((RegexpNode) yyVals[0 + yyTop]).getOptions();
    Node node = ((Node) yyVals[-1 + yyTop]);
    if (node == null) {
        yyVal = new RegexpNode(((Token) yyVals[-2 + yyTop]).getPosition(), ByteList.create(""), options & ~ReOptions.RE_OPTION_ONCE);
    } else if (node instanceof StrNode) {
        yyVal = new RegexpNode(((Node) yyVals[-1 + yyTop]).getPosition(), (ByteList) ((StrNode) node).getValue().clone(), options & ~ReOptions.RE_OPTION_ONCE);
    } else if (node instanceof DStrNode) {
        yyVal = new DRegexpNode(((Token) yyVals[-2 + yyTop]).getPosition(), (DStrNode) node, options, (options & ReOptions.RE_OPTION_ONCE) != 0);
    } else {
        yyVal = new DRegexpNode(((Token) yyVals[-2 + yyTop]).getPosition(), options, (options & ReOptions.RE_OPTION_ONCE) != 0).add(node);
    }
    return yyVal;
}
