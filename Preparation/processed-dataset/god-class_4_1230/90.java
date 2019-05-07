public static Object case12_line331(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new VAliasNode(((Token) yyVals[-2 + yyTop]).getPosition(), (String) ((Token) yyVals[-1 + yyTop]).getValue(), "$" + ((BackRefNode) yyVals[0 + yyTop]).getType());
    return yyVal;
}
