public static Object case297_line1136(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* ENEBO: Lots of optz in 1.9 parser here*/
    yyVal = new ForNode(((Token) yyVals[-8 + yyTop]).getPosition(), ((Node) yyVals[-7 + yyTop]), ((Node) yyVals[-1 + yyTop]), ((Node) yyVals[-4 + yyTop]));
    return yyVal;
}
