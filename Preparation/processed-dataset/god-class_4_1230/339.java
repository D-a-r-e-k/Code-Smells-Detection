public static Object case4_line305(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (((Node) yyVals[-1 + yyTop]) instanceof BlockNode) {
        support.checkUselessStatements(((BlockNode) yyVals[-1 + yyTop]));
    }
    yyVal = ((Node) yyVals[-1 + yyTop]);
    return yyVal;
}
