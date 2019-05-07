public static Object case513_line1983(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    ISourcePosition pos;
    if (((Node) yyVals[-2 + yyTop]) == null && ((Node) yyVals[0 + yyTop]) == null) {
        pos = ((Token) yyVals[-1 + yyTop]).getPosition();
    } else {
        pos = ((Node) yyVals[-2 + yyTop]).getPosition();
    }
    yyVal = support.newArrayNode(pos, ((Node) yyVals[-2 + yyTop])).add(((Node) yyVals[0 + yyTop]));
    return yyVal;
}
