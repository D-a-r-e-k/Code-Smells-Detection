public static Object case23_line380(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    support.checkExpression(((Node) yyVals[0 + yyTop]));
    yyVal = support.node_assign(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
    return yyVal;
}
