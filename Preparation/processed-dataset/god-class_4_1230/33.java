public static Object case216_line860(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "===", ((Node) yyVals[0 + yyTop]), lexer.getPosition());
    return yyVal;
}
