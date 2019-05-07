public static Object case429_line1651(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    lexer.setStrTerm(((StrTerm) yyVals[-2 + yyTop]));
    yyVal = support.newEvStrNode(((Token) yyVals[-3 + yyTop]).getPosition(), ((Node) yyVals[-1 + yyTop]));
    return yyVal;
}
