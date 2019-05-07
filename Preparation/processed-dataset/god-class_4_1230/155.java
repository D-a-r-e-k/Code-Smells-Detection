public static Object case402_line1512(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* FIXME: We may be intern'ing more than once.*/
    yyVal = new SymbolNode(((Token) yyVals[0 + yyTop]).getPosition(), ((String) ((Token) yyVals[0 + yyTop]).getValue()).intern());
    return yyVal;
}
