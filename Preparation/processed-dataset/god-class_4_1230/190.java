public static Object case514_line1993(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    ISourcePosition pos = ((Token) yyVals[-1 + yyTop]).getPosition();
    yyVal = support.newArrayNode(pos, new SymbolNode(pos, (String) ((Token) yyVals[-1 + yyTop]).getValue())).add(((Node) yyVals[0 + yyTop]));
    return yyVal;
}
