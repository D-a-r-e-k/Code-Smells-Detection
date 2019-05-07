public static Object case11_line328(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new VAliasNode(((Token) yyVals[-2 + yyTop]).getPosition(), (String) ((Token) yyVals[-1 + yyTop]).getValue(), (String) ((Token) yyVals[0 + yyTop]).getValue());
    return yyVal;
}
