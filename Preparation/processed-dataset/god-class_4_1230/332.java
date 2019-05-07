public static Object case44_line459(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new ReturnNode(((Token) yyVals[-1 + yyTop]).getPosition(), support.ret_args(((Node) yyVals[0 + yyTop]), ((Token) yyVals[-1 + yyTop]).getPosition()));
    return yyVal;
}
