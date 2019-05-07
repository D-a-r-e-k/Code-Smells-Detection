public static Object case500_line1925(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new UnnamedRestArgNode(((Token) yyVals[0 + yyTop]).getPosition(), support.getCurrentScope().addVariable("*"));
    return yyVal;
}
