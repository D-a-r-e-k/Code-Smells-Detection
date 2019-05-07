public static Object case250_line970(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    ISourcePosition pos = ((Node) yyVals[0 + yyTop]) == null ? lexer.getPosition() : ((Node) yyVals[0 + yyTop]).getPosition();
    yyVal = support.newArrayNode(pos, ((Node) yyVals[0 + yyTop]));
    return yyVal;
}
