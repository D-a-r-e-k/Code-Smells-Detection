public static Object case427_line1643(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    lexer.setStrTerm(((StrTerm) yyVals[-1 + yyTop]));
    yyVal = new EvStrNode(((Token) yyVals[-2 + yyTop]).getPosition(), ((Node) yyVals[0 + yyTop]));
    return yyVal;
}
