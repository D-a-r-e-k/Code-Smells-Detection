public static Object case272_line1056(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    ISourcePosition position = ((Token) yyVals[-2 + yyTop]).getPosition();
    if (((Node) yyVals[-1 + yyTop]) == null) {
        yyVal = new ZArrayNode(position);
    } else {
        yyVal = ((Node) yyVals[-1 + yyTop]);
        ((ISourcePositionHolder) yyVal).setPosition(position);
    }
    return yyVal;
}
